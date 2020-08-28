package me.yushust.message.core.internal;

import me.yushust.message.core.*;
import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.localization.LanguageProvider;

import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.UnaryOperator;

public class SimpleMessageProvider<T> implements MessageProvider<T> {

    private final MessageConsumer<T> messageConsumer;
    private final InterceptManager<T> interceptManager;
    private final MessageRepository messageRepository;
    private final LanguageProvider<T> languageProvider;

    public SimpleMessageProvider(MessageConsumer<T> messageConsumer, InterceptManager<T> interceptManager,
                                 MessageRepository messageRepository, LanguageProvider<T> languageProvider) {
        this.messageConsumer = messageConsumer;
        this.interceptManager = interceptManager;
        this.messageRepository = messageRepository;
        this.languageProvider = languageProvider;
        // ends the construction stage
        this.interceptManager.setMessageProvider(this);
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath, Collection<String> linkedPaths) {

        requireNonNull(propertyHolder);
        requireNonNull(messagePath);
        requireNonNull(linkedPaths);

        String language = languageProvider.getLanguage(propertyHolder);
        String message = messageRepository.getMessage(language, messagePath);

        if (linkedPaths.contains(messagePath)) {
            return message;
        }

        InterceptContext<T> context = new InterceptContext<>(this, propertyHolder);

        if (message == null) {
            return null;
        }

        return interceptManager.convert(context, message, linkedPaths);
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath) {
        return getMessage(propertyHolder, messagePath, createLinkedPathsCollection());
    }

    @Override
    public StringList getMessages(T propertyHolder, String messagePath) {

        requireNonNull(propertyHolder, messagePath);
        requireNonNull(messagePath, messagePath);

        InterceptContext<T> context = new InterceptContext<>(this, propertyHolder);
        String language = languageProvider.getLanguage(propertyHolder);
        StringList messages = messageRepository.getMessages(language, messagePath);

        messages.replaceAll(
                line -> {
                    if (line == null) {
                        return null;
                    }
                    return interceptManager.convert(context, line, createLinkedPathsCollection());
                }
        );

        return messages;
    }

    @Override
    public LanguageProvider<T> getLanguageProvider() {
        return languageProvider;
    }

    @Override
    public InterceptManager<T> getInterceptionManager() {
        return interceptManager;
    }

    @Override
    public void sendMessage(T propertyHolder, String messagePath) {
        messageConsumer.sendMessage(propertyHolder, getMessage(propertyHolder, messagePath));
    }

    @Override
    public void sendMessage(Iterable<T> propertyHolders, String messagePath, UnaryOperator<String> replacer) {
        for (T propertyHolder : propertyHolders) {
            String message = getMessage(propertyHolder, messagePath);
            message = replacer.apply(message);
            messageConsumer.sendMessage(propertyHolder, message);
        }
    }

    @Override
    public String getMessage(@Nullable String language, String messagePath) {
        return messageRepository.getMessage(language, messagePath);
    }

    @Override
    public StringList getMessages(@Nullable String language, String messagePath) {
        return messageRepository.getMessages(language, messagePath);
    }

    private Collection<String> createLinkedPathsCollection() {
        // order isn't important
        return new HashSet<>();
    }

}
