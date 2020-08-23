package me.yushust.message.core;

import me.yushust.message.core.intercept.MessageInterceptor;

/**
 * The library main class
 * @param <T> The type of the property holder
 */
public interface MessageProvider<T> extends MessageRepository, MessageFormatter<T> {

    /**
     * Sends a message to the specified receiver using
     * {@link MessageConsumer}
     * @param propertyHolder The message receiver
     * @param messagePath The message location
     */
    void sendMessage(T propertyHolder, String messagePath);

    /**
     * Sends a message to the specified receivers using
     * {@link MessageConsumer}
     * @param propertyHolders The message receivers
     * @param messagePath The message location
     * @param interceptor The just-in-time interceptor, util for
     *                    replacing variables
     */
    void sendMessage(Iterable<T> propertyHolders, String messagePath, MessageInterceptor interceptor);

    /**
     * Sends a message to the specified receivers using
     * {@link MessageConsumer}
     * @param propertyHolders The message receivers
     * @param messagePath The message location
     */
    default void sendMessage(Iterable<T> propertyHolders, String messagePath) {
        sendMessage(propertyHolders, messagePath, MessageInterceptor.identity());
    }

}
