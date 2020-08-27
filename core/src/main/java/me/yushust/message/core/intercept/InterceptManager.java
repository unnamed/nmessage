package me.yushust.message.core.intercept;

import java.util.Optional;

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
    InterceptManager<T> add(MessageInterceptor<T> interceptor);

    /**
     * Adds a placeholder replacer that corresponds to the
     * specified placeholders.
     * @param replacer The placeholder replacer
     * @return The intercept manager, for a fluent api
     */
    InterceptManager<T> addReplacer(PlaceholderReplacer<T> replacer);

    /**
     * Finds a placeholder replacer for the specified placeholder
     * @param placeholder The placeholder
     * @return The placeholder replacer, wrapped with Optional
     */
    Optional<PlaceholderReplacer<T>> findReplacer(String placeholder);

    /**
     * Calls all message interceptors and placeholder
     * replacers for the specified property holder
     * and the provided text.
     * @param context The replacing context
     * @param propertyHolder The property holder
     * @param text The text that will be modified
     * @return The text already converted
     */
    String convert(InterceptContext<T> context, String text);

}
