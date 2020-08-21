package me.yushust.message.core;

import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.intercept.MessageInterceptor;
import me.yushust.message.core.internal.SimpleMessageProvider;
import me.yushust.message.core.localization.DummyLanguageProvider;
import me.yushust.message.core.localization.LanguageProvider;
import me.yushust.message.core.placeholder.PlaceholderReplacer;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * A MessageProvider Builder, fluent API.
 * Used to create a MessageProvider
 * @param <T> The property holder type
 */
public final class MessageProviderBuilder<T> {

    // optional values
    private final Set<PlaceholderReplacer<T>> placeholderReplacers = new HashSet<>();
    private final Set<MessageInterceptor> messageInterceptors = new HashSet<>();
    private LanguageProvider<T> languageProvider = DummyLanguageProvider.getInstance();
    private String defaultLanguage = "en";
    private String fileFormat = "lang_%lang%.yml";
    private ProvideStrategy provideStrategy = ProvideStrategy.RETURN_PATH;
    private MessageConsumer<T> messageConsumer = MessageConsumer.dummy();

    // required values
    private NodeFileLoader nodeFileLoader;
    private LoadSource loadSource;

    public MessageProviderBuilder<T> setLoadSource(LoadSource loadSource) {
        this.loadSource = loadSource;
        return this;
    }

    public MessageProviderBuilder<T> setFileFormat(String fileFormat) {
        requireNonNull(fileFormat);
        this.fileFormat = fileFormat;
        return this;
    }

    public MessageProviderBuilder<T> setNodeFileLoader(NodeFileLoader nodeFileLoader) {
        this.nodeFileLoader = nodeFileLoader;
        return this;
    }

    public MessageProviderBuilder<T> setProvideStrategy(ProvideStrategy provideStrategy) {
        requireNonNull(provideStrategy);
        this.provideStrategy = provideStrategy;
        return this;
    }

    public MessageProviderBuilder<T> setDefaultLanguage(String defaultLanguage) {
        requireNonNull(defaultLanguage);
        this.defaultLanguage = defaultLanguage;
        return this;
    }

    public MessageProviderBuilder<T> addPlaceholderReplacer(PlaceholderReplacer<T> replacer) {
        requireNonNull(replacer);
        this.placeholderReplacers.add(replacer);
        return this;
    }

    public MessageProviderBuilder<T> addInterceptor(MessageInterceptor messageInterceptor) {
        requireNonNull(messageInterceptor);
        this.messageInterceptors.add(messageInterceptor);
        return this;
    }

    public MessageProviderBuilder<T> setLanguageProvider(LanguageProvider<T> languageProvider) {
        requireNonNull(languageProvider);
        this.languageProvider = languageProvider;
        return this;
    }

    public MessageProviderBuilder<T> setMessageConsumer(MessageConsumer<T> messageConsumer) {
        requireNonNull(messageConsumer);
        this.messageConsumer = messageConsumer;
        return this;
    }

    public MessageProvider<T> build() {

        if (loadSource == null || nodeFileLoader == null) {
            throw new IllegalStateException("The LoadSource and the NodeFileLoader isn't setted!");
        }

        MessageProvider<T> provider = new SimpleMessageProvider<>(
                loadSource, nodeFileLoader,
                provideStrategy, defaultLanguage,
                fileFormat, messageConsumer,
                languageProvider
        );
        InterceptManager<T> interceptManager = provider.getInterceptionManager();

        for (MessageInterceptor messageInterceptor : messageInterceptors) {
            interceptManager.add(messageInterceptor);
        }

        for (PlaceholderReplacer<T> placeholderReplacer : placeholderReplacers) {
            interceptManager.add(placeholderReplacer);
        }

        return provider;
    }

    public static <T> MessageProviderBuilder<T> create() {
        return new MessageProviderBuilder<>();
    }

}
