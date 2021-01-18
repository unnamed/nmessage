package me.yushust.message.send;

import me.yushust.message.util.StringList;

/**
 * A functional interface that represents a method
 * for sending messages to the specified T object
 *
 * @param <E> The entity type
 */
public interface MessageSender<E> {

  /** Sends a list of messages to the specified receiver */
  default void send(E receiver, String mode, StringList messages) {
    send(receiver, mode, messages.join("\n"));
  }

  /** Sends a message to the specified receiver */
  void send(E receiver, String mode, String message);

}
