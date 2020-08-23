package me.yushust.message.core.placeholder;

import me.yushust.message.core.intercept.MessageInterceptor;

/**
 * A functional interface that uses the property
 * holder to replace some text to other.
 * Similar to {@link MessageInterceptor} but receives
 * a property holder as parameter.
 * @param <T> The property holder type
 * @see String#replace
 */
@FunctionalInterface
public interface PlaceholderReplacer<T> {

    /**
     * Modifies a text and returns it,
     * replaces text using the properties of
     * the specified property holder.
     * @param propertyHolder The property holder
     * @param text The text that will be modified
     * @return The modified text
     */
    String replace(T propertyHolder, String text);

    /**
     * Converts a message interceptor to a PlaceholderReplacer
     * @param interceptor The message interceptor
     * @param <T> The type of the placeholder replacer
     * @return The new placeholder replacer
     */
    static <T> PlaceholderReplacer<T> of(MessageInterceptor interceptor) {
        return (propertyHolder, text) -> interceptor.intercept(text);
    }

}
