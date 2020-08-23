package me.yushust.message.core;

/**
 * A functional interface that represents a method
 * for sending messages to the specified T object
 * @param <T> The property holder and message receiver
 */
public interface MessageConsumer<T> {

    MessageConsumer<?> DUMMY = (receiver, message) -> {};

    /**
     * Sends a message to the specified property holder
     * @param receiver The property holder
     * @param message The message
     */
    void sendMessage(T receiver, String message);

    /**
     * Returns the casted dummy message consumer
     * instance
     * @param <T> The type to be casted
     * @return The message consumer
     */
    @SuppressWarnings("unchecked")
    static <T> MessageConsumer<T> dummy() {
        return (MessageConsumer<T>) DUMMY;
    }

}
