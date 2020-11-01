package me.yushust.message.specific;

/**
 * A functional interface that represents a method
 * for sending messages to the specified T object
 *
 * @param <E> The entity type
 */
public interface Messenger<E> {

  Messenger<?> DUMMY = (receiver, message) -> {
  };

  /**
   * Sends a message to the specified property holder
   *
   * @param receiver The property holder
   * @param message  The message
   */
  void send(E receiver, String message);

  /**
   * Returns the casted dummy message consumer
   * instance
   *
   * @param <T> The type to be casted
   * @return The message consumer
   */
  @SuppressWarnings("unchecked")
  static <T> Messenger<T> dummy() {
    return (Messenger<T>) DUMMY;
  }

}