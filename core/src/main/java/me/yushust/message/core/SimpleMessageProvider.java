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
    private final String fileFormat;
    private final String defaultLanguageFile;
    private LanguageProvider<T> languageProvider;

    public SimpleMessageProvider(LoadSource loadSource, NodeFileLoader nodeFileLoader,
                                 LanguageProvider<T> languageProvider, String fileFormat) {
        this.nodeFileAllocator = new SimpleFileAllocator(nodeFileLoader, loadSource);
        this.languageProvider = languageProvider;
        this.fileFormat = fileFormat;
        this.defaultLanguageFile = fileFormat.replace("%lang%", DEFAULT_LANGUAGE);
    }

    public SimpleMessageProvider(LoadSource loadSource, NodeFileLoader nodeFileLoader, String fileFormat) {
        this(loadSource, nodeFileLoader, DummyLanguageProvider.getInstance(), fileFormat);
    }

    public SimpleMessageProvider(LoadSource loadSource, NodeFileLoader nodeFileLoader) {
        this(loadSource, nodeFileLoader, "lang_%lang%.yml");
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath) {
        NodeFile nodeFile = getNodeFileFor(propertyHolder);
        Optional<String> optionalMessage = nodeFile.getString(messagePath);
        if (!optionalMessage.isPresent()) {
            Optional<NodeFile> defaultLanguageFile = nodeFileAllocator.find(defaultLanguageFile);
            if (defaultLanguageFile.isPresent()) {
                optionalMessage = defaultLanguageFile.getString(messagePath);
            }
            if (!optionalMessage.isPresent()) {
                return "Message not found: " + messagePath;
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

}
