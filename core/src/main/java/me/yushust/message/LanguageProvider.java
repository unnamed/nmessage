package me.yushust.message;

import org.jetbrains.annotations.Nullable;

/**
 * A class that provides information about
 * the language of the provided E object.
 *
 * @param <E> The entity type
 */
public interface LanguageProvider<E> {

  LanguageProvider<?> DUMMY = languageHolder -> null;

  /**
   * Gets the language of the specified
   * property holder.
   *
   * @param entity The entity, language holder
   * @return The language of that holder.
   * Returning null = use default language
   */
  @Nullable
  String getLanguage(E entity);

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
