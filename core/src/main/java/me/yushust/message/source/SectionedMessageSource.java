package me.yushust.message.source;

import me.yushust.message.util.Validate;

/**
 * Represents a {@link MessageSource} that
 * holds a {@code prefix} and delegates the
 * value getting to another source.
 *
 * <p>The prefix is always prepended when
 * getting a value from the linked source</p>
 *
 * <p>This class doesn't override the default
 * behaviour when getting a sectioned source
 * specified in {@link MessageSource#getSection}
 * to avoid calls added to the call stack</p>
 */
public class SectionedMessageSource
    implements MessageSource {

  private final MessageSource parent;
  private final String prefix;

  private SectionedMessageSource(
      MessageSource parent,
      String prefix
  ) {
    this.parent = parent;
    this.prefix = prefix;
  }

  @Override
  public Object get(String path) {
    return parent.get(prefix + path);
  }

  @Override
  public String getSectionSeparator() {
    // maintain the same section separator across child classes
    return parent.getSectionSeparator();
  }

  /**
   * Creates a new sectioned message source linked
   * to the specified {@code parent} and using the
   * given {@code path} to obtain all the messages
   * @param parent The delegated message source
   * @param path The linked path
   * @return The message source, it's {@code parent} if
   * {@code path} is empty.
   */
  public static MessageSource of(
      MessageSource parent,
      String path
  ) {
    Validate.isNotNull(parent, "parent");
    Validate.isNotNull(path, "path");
    if (path.isEmpty()) {
      return parent;
    } else {
      return new SectionedMessageSource(
          parent,
          path + parent.getSectionSeparator()
      );
    }
  }

}