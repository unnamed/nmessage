package me.yushust.message.core.intercept;

/**
 * A functional interface that intercepts
 * messages independently of property holder type
 * Similar to {@link java.util.function.UnaryOperator}
 * with String as type parameter
 */
@FunctionalInterface
public interface MessageInterceptor {

    /**
     * Intercepts a message
     * @param text The text
     * @return The new message
     */
    String intercept(String text);

    /**
     * Returns a message interceptor that
     * doesn't modify the message and returns
     * the same message.
     * @return A "dummy" message interceptor
     */
    static MessageInterceptor identity() {
        return text -> text;
    }

}
