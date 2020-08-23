package me.yushust.message.core.internal;

import me.yushust.message.core.*;
import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.holder.allocate.SimpleFileAllocator;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.intercept.MessageInterceptor;
import me.yushust.message.core.localization.LanguageProvider;
import org.jetbrains.annotations.Nullable;

public class SimpleMessageProvider<T> extends SimpleMessageFormatter<T> implements MessageProvider<T> {

    private final MessageConsumer<T> messageConsumer;
    private final MessageRepository messageRepository;
    private final MessageFormatter<T> messageFormatter;

    public SimpleMessageProvider(MessageRepository messageRepository, LanguageProvider<T> languageProvider,
                                 MessageConsumer<T> messageConsumer) {
        super(messageRepository, languageProvider);
        this.messageRepository = messageRepository;
        this.messageFormatter = new SimpleMessageFormatter<>(this, languageProvider);
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void sendMessage(T propertyHolder, String messagePath) {
        messageConsumer.sendMessage(propertyHolder, getMessage(propertyHolder, messagePath));
    }

    @Override
    public void sendMessage(Iterable<T> propertyHolders, String messagePath, MessageInterceptor replacer) {
        for (T propertyHolder : propertyHolders) {
            String message = getMessage(propertyHolder, messagePath);
            message = replacer.intercept(message);
            messageConsumer.sendMessage(propertyHolder, message);
        }
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath) {
        return messageFormatter.getMessage(propertyHolder, messagePath);
    }

    @Override
    public StringList getMessages(T propertyHolder, String messagePath) {
        return messageFormatter.getMessages(propertyHolder, messagePath);
    }

    @Override
    public void useLanguageProvider(LanguageProvider<T> languageProvider) {
        messageFormatter.useLanguageProvider(languageProvider);
    }

    @Override
    public InterceptManager<T> getInterceptionManager() {
        return messageFormatter.getInterceptionManager();
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
                                                String defaultLanguage) {
        return new SimpleMessageProvider<>(
                new SimpleMessageRepository(
                        new SimpleFileAllocator(nodeFileLoader, loadSource),
                        provideStrategy,
                        fileFormat,
                        defaultLanguage
                ),
                languageProvider,
                messageConsumer
        );
    }

}
