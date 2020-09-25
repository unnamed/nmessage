package me.yushust.message.localization;

import org.jetbrains.annotations.Nullable;

/**
 * A class that provides information about
 * the language of the provided T object.
 *
 * @param <T> The property holder type
 */
public interface LanguageProvider<T> {

  LanguageProvider<?> DUMMY = languageHolder -> null;

  /**
   * Gets the language of the specified
   * property holder.
   *
   * @param languageHolder Property holder
   * @return The language of that holder.
   * Returning null = use default language
   */
  @Nullable
  String getLanguage(T languageHolder);

  /**
   * The dummy language provider instance
   * casted to "T"
   *
   * @param <T> The type to convert
   * @return The language provider casted to "T"
   */
  @SuppressWarnings("unchecked")
  static <T> LanguageProvider<T> dummy() {
    return (LanguageProvider<T>) DUMMY;
  }

}
