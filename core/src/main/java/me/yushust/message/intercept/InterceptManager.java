package me.yushust.message.intercept;

import me.yushust.message.MessageHandler;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.provide.ProvideContext;

/**
 * Manages the message interception, either with simple
 * {@link MessageInterceptor} or with {@link PlaceholderProvider}
 *
 * @param <E> The entity type
 */
public interface InterceptManager<E> extends FormatterRegistry<E> {

  /**
   * Sets the intercept manager message handler
   *
   * @param handle The message handler
   * @throws IllegalStateException If the handler is
   *                               already defined
   */
  void setHandle(MessageHandler<E> handle);

  /**
   * Calls all message interceptors and placeholder
   * replacers for the specified property holder
   * and the provided text.
   *
   * @param context The replacing context
   * @param text    The text that will be modified
   * @return The text already converted
   */
  String convert(ProvideContext<E> context, String text);

}
