package me.yushust.message;

import me.yushust.message.internal.MessageRepositoryBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * A part of the principal interface of this
 * library. Handles stored messages, adds
 * format to them and replaces variables.
 */
public interface MessageRepository {

  /**
   * Finds the message using the language and the messagePath
   * If the message is not found, it returns null
   * or the path as specified with {@link ProvideStrategy}
   *
   * @param language    The language
   * @param messagePath The message location
   * @return The message, null or the path.
   */
  String getMessage(@Nullable String language, String messagePath);

  /**
   * Finds the message using the default language.
   * If the message is not found, it calls
   * to the previously provided {@link ProvideStrategy}
   *
   * @param messagePath The message locatoin
   * @return The message
   */
  default String getMessage(String messagePath) {
    return getMessage(null, messagePath);
  }

  /**
   * Finds the messages using the language and the messagePath
   * If the messages are not found, it returns a singleton
   * list containing null or the path as specified
   * with {@link ProvideStrategy}
   *
   * @param language    The language
   * @param messagePath The message location
   * @return The messages, wrapped with StringList
   */
  StringList getMessages(@Nullable String language, String messagePath);

  /**
   * Finds the messages using the default language and
   * the specified message path. If the messages are
   * not found, it calls to the previously provided
   * {@link ProvideStrategy} and returns a singleton
   * {@link StringList}
   *
   * @param messagePath The message location
   * @return The message
   */
  default StringList getMessages(String messagePath) {
    return getMessages(null, messagePath);
  }

  static MessageRepositoryBuilder builder() {
    return new MessageRepositoryBuilder();
  }

}
