package me.yushust.message.core;

import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFile;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.holder.allocate.NodeFileAllocator;
import me.yushust.message.core.holder.allocate.SimpleFileAllocator;
import me.yushust.message.core.intercept.ReplacingMessageInterceptor;
import me.yushust.message.core.intercept.SimpleMessageInterceptor;
import me.yushust.message.core.placeholder.PlaceholderApplier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class SimpleMessageProvider<T> implements MessageProvider<T> {

    private final ReplacingMessageInterceptor<T> interceptor = new SimpleMessageInterceptor<>();

    private final NodeFileAllocator nodeFileAllocator;
    private final ProvideStrategy provideStrategy;
    private final String fileFormat;
    private final String defaultLanguageFile;
    private final MessageConsumer<T> messageConsumer;
    private LanguageProvider<T> languageProvider;

    SimpleMessageProvider(LoadSource loadSource, NodeFileLoader nodeFileLoader,
                          ProvideStrategy provideStrategy, String defaultLanguage,
                          String fileFormat, MessageConsumer<T> messageConsumer) {
        this.nodeFileAllocator = new SimpleFileAllocator(nodeFileLoader, loadSource);
        this.fileFormat = fileFormat;
        this.provideStrategy = provideStrategy;
        this.defaultLanguageFile = fileFormat.replace("%lang%", defaultLanguage);
        this.languageProvider = new DummyLanguageProvider<>(defaultLanguage);
        this.messageConsumer = messageConsumer;
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath) {
        NodeFile nodeFile = getNodeFileFor(propertyHolder);
        Optional<String> optionalMessage = nodeFile.getString(messagePath);
        if (!optionalMessage.isPresent()) {
            Optional<NodeFile> defaultLanguage = nodeFileAllocator.find(defaultLanguageFile);
            if (defaultLanguage.isPresent()) {
                optionalMessage = defaultLanguage.get().getString(messagePath);
            }
            if (!optionalMessage.isPresent()) {
                if (provideStrategy == ProvideStrategy.RETURN_NULL) {
                    return null;
                } else {
                    return messagePath;
                }
            }
        }
        return format(propertyHolder, optionalMessage.get());
    }

    @Override
    public StringList getMessages(T propertyHolder, String messagePath) {
        NodeFile nodeFile = getNodeFileFor(propertyHolder);
        List<String> messages = nodeFile.getStringList(messagePath);
        if (messages.isEmpty()) {
            return new StringList(
                    Collections.singletonList("Messages not found: " + messagePath)
            );
        }
        StringList stringList = new StringList(messages);
        stringList.getContents().replaceAll(
                line -> format(propertyHolder, line)
        );
        return stringList;
    }

    @Override
    public LanguageProvider<T> getLanguageProvider() {
        return languageProvider;
    }

    @Override
    public void useLanguageProvider(LanguageProvider<T> languageProvider) {
        requireNonNull(languageProvider);
        this.languageProvider = languageProvider;
    }

    @Override
    public ReplacingMessageInterceptor<T> getInterceptor() {
        return interceptor;
    }

    private String format(T propertyHolder, String text) {
        String formattedText = text;
        PlaceholderApplier<T> applier = interceptor.getPlaceholderApplier();
        formattedText = applier.applyPlaceholders(propertyHolder, formattedText);
        formattedText = interceptor.intercept(formattedText);
        return formattedText;
    }

    private NodeFile getNodeFileFor(T propertyHolder) {
        String language = languageProvider.getLanguage(propertyHolder);
        return nodeFileAllocator.find(fileFormat.replace("%lang%", language))
                .orElseGet(
                        () -> nodeFileAllocator.find(defaultLanguageFile)
                            .orElseThrow(() -> new IllegalStateException("There's no a default language file or resource!"))
                );
    }

    @Override
    public void sendMessage(T propertyHolder, String messagePath) {
        messageConsumer.sendMessage(propertyHolder, getMessage(propertyHolder, messagePath));
    }

    @Override
    public void sendMessage(Iterable<T> propertyHolders, String messagePath) {
        for (T propertyHolder : propertyHolders) {
            sendMessage(propertyHolder, messagePath);
        }
    }
}
