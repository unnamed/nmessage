package me.yushust.message.core.localization;

import org.jetbrains.annotations.Nullable;

/**
 * A class that provides information about
 * the language of the provided T object.
 * @param <T> The property holder type
 */
public interface LanguageProvider<T> {

    /**
     * Gets the language of the specified
     * property holder.
     * @param languageHolder Property holder
     * @return The language of that holder.
     * Returning null = use default language
     */
    @Nullable
    String getLanguage(T languageHolder);

}
