package me.yushust.message.core.provide;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.intercept.MessageInterceptor;
import me.yushust.message.core.placeholder.PlaceholderProvider;

/**
 * Represents a context of interception, passed to
 * {@link MessageInterceptor} and {@link PlaceholderProvider}
 * @param <T> The entity type
 */
public class InterceptContext<T> {
    
    private final MessageProvider<T> provider;
    private final T entity;

    public InterceptContext(MessageProvider<T> provider, T entity) {
        this.provider = provider;
        this.entity = entity;
    }

    public MessageProvider<T> getProvider() {
        return provider;
    }

    public T getEntity() {
        return entity;
    }

}