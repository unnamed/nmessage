package me.yushust.message.core.intercept;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.placeholder.PlaceholderProvider;

/**
 * Represents a context of interception, passed to
 * {@link MessageInterceptor} and {@link PlaceholderProvider}
 * @param <T> The entity type
 */
public final class InterceptContext<T> {
    
    private final MessageProvider<T> messageProvider;
    private final T entity;

    public InterceptContext(MessageProvider<T> messageProvider, T entity) {
        this.messageProvider = messageProvider;
        this.entity = entity;
    }

    public MessageProvider<T> getMessageProvider() {
        return messageProvider;
    }

    public T getEntity() {
        return entity;
    }

}