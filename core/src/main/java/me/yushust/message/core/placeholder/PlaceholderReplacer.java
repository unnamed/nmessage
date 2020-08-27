package me.yushust.message.core.placeholder;

import org.jetbrains.annotations.Nullable;

import me.yushust.message.core.intercept.InterceptContext;

/**
 * A replacer that uses the property
 * holder to get values for a range of placeholders.
 * @param <T> The property holder type
 */
public interface PlaceholderReplacer<T> {

    /**
     * @return An array of acceptable placeholders
     */
    String[] getPlaceholders();

    /**
     * Returns a value corresponding to the specified
     * placeholder using the properties of
     * the specified property holder.
     * @param context The context containing the property holder
     *                and the MessageProvider
     * @param placeholder The replacing placeholder
     * @return The modified text
     */
    @Nullable
    String replace(InterceptContext<T> context, String placeholder);

}
