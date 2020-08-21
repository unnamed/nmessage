package me.yushust.message.core;

import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.intercept.MessageInterceptor;
import me.yushust.message.core.placeholder.PlaceholderApplier;

import java.util.HashSet;
import java.util.Set;

public final class MessageProviderBuilder<T> {

    private final Set<PlaceholderApplier<T>> placeholderReplacers = new HashSet<>();
    private final Set<MessageInterceptor> messageInterceptors = new HashSet<>();
    private LanguageProvider<T> languageProvider;
    private String defaultLanguage = "en";
    private String fileFormat = "lang_%lang%.yml";
    private ProvideStrategy provideStrategy = ProvideStrategy.RETURN_PATH;
    private NodeFileLoader nodeFileLoader;
    private LoadSource loadSource;
    private MessageConsumer<T> messageConsumer = MessageConsumer.dummy();

    public MessageProviderBuilder<T> withLoadSource(LoadSource loadSource) {
        this.loadSource = loadSource;
        return this;
    }

    public MessageProviderBuilder<T> fileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
        return this;
    }

    public MessageProviderBuilder<T> nodeFileLoader(NodeFileLoader nodeFileLoader) {
        this.nodeFileLoader = nodeFileLoader;
        return this;
    }

    public MessageProviderBuilder<T> provideStrategy(ProvideStrategy provideStrategy) {
        this.provideStrategy = provideStrategy;
        return this;
    }

    public MessageProviderBuilder<T> defaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
        return this;
    }

    public MessageProviderBuilder<T> addPlaceholderReplacer(PlaceholderApplier<T> replacer) {
        this.placeholderReplacers.add(replacer);
        return this;
    }

    public MessageProviderBuilder<T> addInterceptor(MessageInterceptor messageInterceptor) {
        this.messageInterceptors.add(messageInterceptor);
        return this;
    }

    public MessageProviderBuilder<T> usingLanguageProvider(LanguageProvider<T> languageProvider) {
        this.languageProvider = languageProvider;
        return this;
    }

    public MessageProviderBuilder<T> withMessageConsumer(MessageConsumer<T> messageConsumer) {
        this.messageConsumer = messageConsumer;
        return this;
    }

    public MessageProvider<T> build() {

        MessageProvider<T> provider = new SimpleMessageProvider<>(
                loadSource, nodeFileLoader,
                provideStrategy, defaultLanguage,
                fileFormat, messageConsumer
        );

        if (languageProvider != null) {
            provider.useLanguageProvider(languageProvider);
        }

        for (MessageInterceptor messageInterceptor : messageInterceptors) {
            provider.getInterceptor().addInterceptor(messageInterceptor);
        }

        for (PlaceholderApplier<T> placeholderApplier : placeholderReplacers) {
            provider.getInterceptor().addPlaceholderApplier(placeholderApplier);
        }

        return provider;
    }

    public static <T> MessageProviderBuilder<T> create() {
        return new MessageProviderBuilder<>();
    }

}
