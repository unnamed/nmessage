package me.yushust.message.core;

public interface MessageConsumer<T> {

    MessageConsumer<?> dummy = (receiver, message) -> {};

    void sendMessage(T receiver, String message);

    @SuppressWarnings("unchecked")
    static <T> MessageConsumer<T> dummy() {
        return (MessageConsumer<T>) dummy;
    }

}
