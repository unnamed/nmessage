package me.yushust.message.core.internal;

import me.yushust.message.core.*;
import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.localization.LanguageProvider;

import me.yushust.message.core.provide.ProvideContext;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

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
    public String getMessage(ProvideContext<T> context, String messagePath) {

        requireNonNull(context);
        requireNonNull(messagePath);

        String language = languageProvider.getLanguage(context.getEntity());
        String message = messageRepository.getMessage(language, messagePath);

        if (context.getLinkedPaths().contains(messagePath)) {
            return message;
        }

        context.getLinkedPaths().add(messagePath);

        if (message == null) {
            return null;
        }

        return interceptManager.convert(context, message);
    }

    @Override
    public String getMessage(T entity, String messagePath) {
        return getMessage(new ProvideContext<>(this, entity), messagePath);
    }

    @Override
    public StringList getMessages(T propertyHolder, String messagePath) {

        requireNonNull(propertyHolder, messagePath);
        requireNonNull(messagePath, messagePath);

        ProvideContext<T> context = new ProvideContext<>(this, propertyHolder);
        String language = languageProvider.getLanguage(propertyHolder);
        StringList messages = messageRepository.getMessages(language, messagePath);

        messages.replaceAll(
                line -> {
                    if (line == null) {
                        return null;
                    }
                    return interceptManager.convert(context, line);
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
