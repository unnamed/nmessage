package me.yushust.message.source;

import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a {@link MessageSource} that uses
 * a file name and a class {@code T} representing
 * the loaded data.
 * @param <T> The class representing the loaded data,
 *           for example, for a properties implementation
 *           it should be {@link java.util.Properties}
 */
public abstract class AbstractCachedFileSource<T>
  implements MessageSource {

  /**
   * The language placeholder to difference the
   * filenames by languages. File formats must
   * contain this variable.
   */
  private static final String LANGUAGE_VARIABLE = "%lang%";

  /**
   * Sentinel value that indicates that
   * a message source isn't present for
   * a specific language
   */
  protected static final Object ABSENT =
    new Object();

  /**
   * Map that contains the stored
   * results while obtaining a message
   * source. Relation of (language -> result)
   */
  protected final Map<String, Object> cache =
    new HashMap<>();

  protected final String fileFormat;

  protected AbstractCachedFileSource(String fileFormat) {
    Validate.isNotEmpty(fileFormat);
    Validate.isTrue(
      fileFormat.contains(LANGUAGE_VARIABLE),
      "File format (" + fileFormat + ") must contain " +
        "the language variable (" + LANGUAGE_VARIABLE + ')'
    );
    this.fileFormat = fileFormat;
  }

  @Override
  public Object get(@Nullable String language, String path) {

    if (language == null) {
      return null;
    }

    Object source = cache.get(language);

    if (source == ABSENT) {
      return null;
    } else if (source == null) {
      source = getSource(getFilename(language));
      if (source == null) {
        cache.put(language, ABSENT);
        return null;
      }
    }

    @SuppressWarnings("unchecked")
    T castedSource = (T) source;
    cache.put(language, source);
    return getValue(castedSource, path);
  }

  /**
   * Removes all the loaded data about
   * the language files from the internal
   * cache map
   */
  public void invalidateCaches() {
    cache.clear();
  }

  protected String getFilename(String language) {
    return fileFormat.replace(LANGUAGE_VARIABLE, language);
  }

  @Nullable
  protected abstract Object getValue(T source, String path);

  @Nullable
  protected abstract T getSource(String filename);

}
