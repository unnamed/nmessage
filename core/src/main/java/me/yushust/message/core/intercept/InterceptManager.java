package me.yushust.message.core.intercept;

import me.yushust.message.core.placeholder.PlaceholderReplacer;

/**
 * Manages the message interception, either with simple
 * {@link MessageInterceptor} or with {@link PlaceholderReplacer}
 * @param <T> The property holder type
 */
public interface InterceptManager<T> {

    /**
     * Adds a message interceptor to the list
     * of message interceptors
     * @param interceptor The new message interceptor
     * @return The intercept manager, for a fluent api
     */
    InterceptManager<T> add(MessageInterceptor interceptor);

    /**
     * Adds a placeholder replacer to the list
     * of placeholder replacers
     * @param replacer The new placeholder replacer
     * @return The intercept mananager, for a fluent api
     */
    InterceptManager<T> add(PlaceholderReplacer<T> replacer);

    /**
     * Calls all message interceptors and placeholder
     * replacers for the specified property holder
     * and the provided text.
     * @param propertyHolder The property holder
     * @param text The text that will be modified
     * @return The text already converted
     */
    String convert(T propertyHolder, String text);

}
