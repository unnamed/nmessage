package me.yushust.message.core.internal;

import me.yushust.message.core.*;
import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.holder.allocate.SimpleFileAllocator;
import me.yushust.message.core.intercept.DefaultInterceptManager;
import me.yushust.message.core.intercept.InterceptContext;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.localization.LanguageProvider;
import me.yushust.message.core.placeholder.PlaceholderBox;

import org.jetbrains.annotations.Nullable;
import static java.util.Objects.requireNonNull;

import java.util.function.UnaryOperator;

public class SimpleMessageProvider<T> implements MessageProvider<T> {

    private final MessageConsumer<T> messageConsumer;
    private final InterceptManager<T> interceptManager = new DefaultInterceptManager<>("path_");
    private final MessageRepository messageRepository;
    private final PlaceholderBox placeholderBox;
    private LanguageProvider<T> languageProvider;

    public SimpleMessageProvider(MessageRepository messageRepository, LanguageProvider<T> languageProvider,
                                 MessageConsumer<T> messageConsumer, PlaceholderBox placeholderBox) {
        this.messageRepository = messageRepository;
        this.languageProvider = languageProvider;
        this.messageConsumer = messageConsumer;
        this.placeholderBox = placeholderBox;
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath) {

        requireNonNull(propertyHolder);
        requireNonNull(messagePath);

        InterceptContext<T> context = new InterceptContext<>(this, propertyHolder);
        String language = languageProvider.getLanguage(propertyHolder);
        String message = messageRepository.getMessage(language, messagePath);

        if (message == null) {
            return null;
        }

        return interceptManager.convert(context, message);
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
                    return interceptManager.convert(context, line);
                }
        );

        return messages;
    }

    @Override
    public void useLanguageProvider(LanguageProvider<T> languageProvider) {
        requireNonNull(languageProvider);
        this.languageProvider = languageProvider;
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

    public static <T> MessageProvider<T> create(LoadSource loadSource, NodeFileLoader nodeFileLoader,
                                                LanguageProvider<T> languageProvider, MessageConsumer<T> messageConsumer,
                                                ProvideStrategy provideStrategy, String fileFormat,
                                                String defaultLanguage, PlaceholderBox placeholderBox) {
        return new SimpleMessageProvider<>(
                new SimpleMessageRepository(
                        new SimpleFileAllocator(nodeFileLoader, loadSource),
                        provideStrategy,
                        fileFormat,
                        defaultLanguage
                ),
                languageProvider,
                messageConsumer,
                placeholderBox
        );
    }

    @Override
    public PlaceholderBox getPlaceholderBox() {
        return placeholderBox;
    }

}
