package me.yushust.message.internal;

import me.yushust.message.format.PlaceholderProvider;
import org.jetbrains.annotations.NotNull;

/**
 * A functional interface that intercepts
 * messages independently of the entity type
 * Similar to {@link java.util.function.UnaryOperator}
 * with String as type parameter
 */
@FunctionalInterface
public interface MessageInterceptor {

  /**
   * Similar to {@link PlaceholderProvider} but receives
   * the original text and not placeholder parameters.
   *
   * @param text The original text
   * @return The intercepted string
   */
  @NotNull
  String intercept(String text);

}
