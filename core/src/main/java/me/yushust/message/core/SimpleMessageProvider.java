package me.yushust.message.core;

import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.holder.allocate.NodeFileAllocator;
import me.yushust.message.core.holder.allocate.SimpleFileAllocator;
import me.yushust.message.core.intercept.ReplacingMessageInterceptor;
import me.yushust.message.core.intercept.SimpleMessageInterceptor;

import static java.util.Objects.requireNonNull;

public class SimpleMessageProvider<T> implements MessageProvider<T> {

    private final ReplacingMessageInterceptor<T> interceptor = new SimpleMessageInterceptor<>();
    private final NodeFileAllocator nodeFileAllocator;
    private LanguageProvider<T> languageProvider;

    public SimpleMessageProvider(LoadSource loadSource, NodeFileLoader nodeFileLoader, LanguageProvider<T> languageProvider) {
        this.nodeFileAllocator = new SimpleFileAllocator(nodeFileLoader, loadSource);
        this.languageProvider = languageProvider;
    }

    public SimpleMessageProvider(LoadSource loadSource, NodeFileLoader nodeFileLoader) {
        this(loadSource, nodeFileLoader, DummyLanguageProvider.getInstance());
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath) {
        String language = languageProvider.getLanguage(propertyHolder);
        // TODO: end this shit
        return null;
    }

    @Override
    public StringList getMessages(T propertyHolder, String messagePath) {
        return null;
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

}
