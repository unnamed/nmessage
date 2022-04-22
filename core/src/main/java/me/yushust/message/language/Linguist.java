package me.yushust.message.language;

import org.jetbrains.annotations.Nullable;

/**
 * A class that provides information about
 * the language of the provided E object.
 *
 * @param <E> The entity type
 */
public interface Linguist<E> {

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

}
