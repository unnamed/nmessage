package me.yushust.message.source;

import org.jetbrains.annotations.Nullable;

/**
 * Represents the source of messages, used to
 * obtain all the messages using the language
 * and a simple string as message identifier
 * called 'path'
 */
public interface MessageSource {

  /**
   * Gets the value in the specified {@code path}.
   * Basic message handler implementations only
   * handle two types: Strings and String Lists
   * @param path The path
   * @return The value in the path
   */
  Object get(@Nullable String language, String path);

  /**
   * Loads the file linked to the specified
   * {@code language}, some implementations doesn't
   * support this operation, so it can throw
   * a {@link UnsupportedOperationException}
   * @throws UnsupportedOperationException If not supported
   * @param language The loaded language
   */
  default void load(String language) {
    throw new UnsupportedOperationException(
      "This message source type doesn't support message pre-loading"
    );
  }

  /**
   * @return The section separator characters, specially
   * used for file message sources that use node files (like
   * YAML or JSON) to separate its sections in 'nodes'
   */
  default char getSectionSeparator() {
    return '.';
  }

  /**
   * Gets a {@link MessageSource} linked to the
   * given {@code path}
   */
  default MessageSource getSection(String path) {
    return SectionedMessageSource.of(this, path);
  }

}