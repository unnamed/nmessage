package me.yushust.message.core;

import me.yushust.message.core.intercept.MessageInterceptor;

public interface MessageProvider<T> extends MessageRepository<T> {

    void sendMessage(T propertyHolder, String messagePath);

    void sendMessage(Iterable<T> propertyHolders, String messagePath, MessageInterceptor interceptor);

    default void sendMessage(Iterable<T> propertyHolders, String messagePath) {
        sendMessage(propertyHolders, messagePath, MessageInterceptor.identity());
    }

}
