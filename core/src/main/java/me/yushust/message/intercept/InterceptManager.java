package me.yushust.message.intercept;

import me.yushust.message.placeholder.PlaceholderBox;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.provide.ProvideContext;

/**
 * Manages the message interception, either with simple
 * {@link MessageInterceptor} or with {@link PlaceholderProvider}
 * @param <T> The entity type
 */
public interface InterceptManager<T> extends FormatterRegistry<T> {

    /**
     * Calls all message interceptors and placeholder
     * replacers for the specified property holder
     * and the provided text.
     * @param context The replacing context
     * @param text The text that will be modified
     * @return The text already converted
     */
    String convert(ProvideContext<T> context, String text);

    /**
     * Sets the new placeholder box or
     * placeholder delimiters.
     * @param box the new placeholder box
     */
    void setPlaceholderBox(PlaceholderBox box);

}
