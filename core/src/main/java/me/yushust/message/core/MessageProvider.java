package me.yushust.message.core;

public interface MessageProvider<T> extends MessageRepository<T> {

    void sendMessage(T propertyHolder, String messagePath);

    void sendMessage(Iterable<T> propertyHolders, String messagePath);

}
