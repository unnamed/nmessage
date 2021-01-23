package me.yushust.message.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Collection of utilities for checking arguments,
 * states, etc.
 */
public final class Validate {

  private Validate() {
    throw new UnsupportedOperationException("This class couldn't be instantiated!");
  }

  /**
   * Checks if the specified object is
   * null. If not null, returns the object.
   * Else, the method results in a
   * {@link NullPointerException} being thrown.
   *
   * @param object The checking object
   * @param name   The name of the object,
   *               used to specify a message
   *               in the exception.
   * @param <T>    The type of the object.
   * @return The object, not null.
   */
  @NotNull
  public static <T> T isNotNull(@Nullable T object, String name) {
    if (object == null) {
      throw new NullPointerException(name);
    } else {
      return object;
    }
  }

  /**
   * Checks if the specified object is
   * null. If not null, returns the object.
   * Else, the method results in a
   * {@link NullPointerException} being thrown.
   *
   * @param object The checking object
   * @param <T>    The type of the object.
   * @return The object, not null.
   */
  @NotNull
  public static <T> T isNotNull(@Nullable T object) {
    return isNotNull(object, "null");
  }

  /**
   * Checks if the specified string is
   * null or empty. If it is, the method
   * results in a {@link IllegalArgumentException}
   * being thrown.
   *
   * @param string The checking object
   * @return The same string, if not null
   * and not empty.
   */
  public static String isNotEmpty(String string) {
    if (string == null || string.isEmpty()) {
      throw new IllegalArgumentException("Provided string is empty or null!");
    } else {
      return string;
    }
  }

  /**
   * Checks if the provided expression is true.
   * If it's true, does nothing. Else, the method
   * results in a {@link IllegalStateException}
   * being thrown.
   *
   * @param expression The evaluated expression
   * @param message    The message for the exception
   *                   if the expression is false.
   */
  public static void isState(boolean expression, String message) {
    if (!expression) {
      throw new IllegalStateException(message);
    }
  }

  /**
   * Checks if the provided expression is true.
   * If it's true, does nothing. Else, the method
   * results in a {@link IllegalArgumentException}
   * being thrown.
   * The same as {@link Validate#isState}, but it
   * throws a {@link IllegalArgumentException}
   * instead of a {@link IllegalStateException}
   *
   * @param expression The evaluated expression
   * @param message    The message for the exception
   *                   if the expression is false.
   */
  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new IllegalArgumentException(message);
    }
  }

}
