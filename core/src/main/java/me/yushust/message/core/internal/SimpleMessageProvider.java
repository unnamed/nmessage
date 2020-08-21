package me.yushust.message.core.internal;

import me.yushust.message.core.MessageConsumer;
import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.ProvideStrategy;
import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.holder.allocate.SimpleFileAllocator;
import me.yushust.message.core.intercept.MessageInterceptor;
import me.yushust.message.core.localization.LanguageProvider;

public class SimpleMessageProvider<T> extends SimpleMessageRepository<T> implements MessageProvider<T> {

    private final MessageConsumer<T> messageConsumer;

    public SimpleMessageProvider(LoadSource loadSource, NodeFileLoader nodeFileLoader,
                          ProvideStrategy provideStrategy, String defaultLanguage,
                          String fileFormat, MessageConsumer<T> messageConsumer,
                          LanguageProvider<T> languageProvider) {
        super(
                new SimpleFileAllocator(nodeFileLoader, loadSource),
                languageProvider,
                provideStrategy,
                fileFormat,
                defaultLanguage
        );
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

}
