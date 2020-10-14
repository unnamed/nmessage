package me.yushust.message;

import me.yushust.message.internal.MessageHandlerBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * The library main class
 *
 * @param <E> The entity type
 */
public interface MessageHandler<E> extends MessageRepository {

  /**
   * Gets a message using the language of the
   * entity (using {@link LanguageProvider}),
   * the message is located using the messagePath.
   * If the message is not found, it returns null
   * or the path as specified with {@link ProvideStrategy}
   *
   * @param entity      The entity, if the entity isn't an
   *                    instance of the specified entity
   *                    type, the provider looks for a
   *                    {@link EntityResolver}
   * @param messagePath The message location
   * @param jitEntities The just-in-time entities used
   *                    to format the getted message
   * @return The message, null or the path.
   */
  String getMessage(Object entity, String messagePath, Object... jitEntities);

  /**
   * Gets many messages using the language of the
   * provided entity (using {@link LanguageProvider}),
   * the messages are located using the messagePath.
   * If the messages aren't found, it returns a
   * singleton list containing null or the path as
   * specified with {@link ProvideStrategy}.
   * The messages are wrapped with {@link StringList}
   * to facilitate their handling.
   *
   * @param entity      The entity, if the entity isn't
   *                    an instance of the specified entity
   *                    class, the provider looks for a
   *                    {@link EntityResolver}
   * @param messagePath The messages location
   * @param jitEntities The just-in-time entities used
   *                    to format the getted message
   * @return The messages, wrapped with StringList
   */
  StringList getMessages(Object entity, String messagePath, Object... jitEntities);

  // Helper methods start
  void sendMessage(Object entity, String messagePath, Object... jitEntities);

  void sendMessage(Iterable<?> entities, String messagePath, Object... jitEntities);

  void sendMessages(Object entity, String messagePath, Object... jitEntities);

  void sendMessages(Iterable<?> entities, String messagePath, Object... jitEntities);
  // Helper methods end

  /**
   * Checks for a {@link EntityResolver}.
   * If present, calls it and returns the entity that the EntityResolver returned,
   * If not present, the method results in a {@link IllegalArgumentException}
   * being thrown
   *
   * @param resolvableEntity The resolvable entity
   * @return The resolved entity
   * @throws IllegalArgumentException If the resolvable entity cannot be
   *                                  converted to a real entity
   */
  @Nullable
  E asEntity(Object resolvableEntity);

  /**
   * @return The language provider that is used to
   * get the entities language.
   */
  LanguageProvider<E> getLanguageProvider();

  FailureListener getFailureListener();

  static <T> MessageHandlerBuilder<T> builder(Class<T> entityType) {
    return new MessageHandlerBuilder<>(entityType);
  }

}
