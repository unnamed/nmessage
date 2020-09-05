package me.yushust.message.provide;

import me.yushust.message.MessageHandler;
import me.yushust.message.intercept.MessageInterceptor;
import me.yushust.message.placeholder.PlaceholderProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a context of interception, passed to
 * {@link MessageInterceptor} and {@link PlaceholderProvider}
 * @param <T> The entity type
 */
public class InterceptContext<T> {
    
    private final MessageHandler<T> provider;
    private final T entity;

    public InterceptContext(MessageHandler<T> provider, @Nullable T entity) {
        this.provider = provider;
        this.entity = entity;
    }

    public MessageHandler<T> getProvider() {
        return provider;
    }

    public T getEntity() {
        return entity;
    }

}