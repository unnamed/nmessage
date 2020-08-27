package me.yushust.message.core.intercept;

import org.jetbrains.annotations.NotNull;

import me.yushust.message.core.placeholder.PlaceholderReplacer;

/**
 * A functional interface that intercepts
 * messages independently of property holder type
 * Similar to {@link java.util.function.UnaryOperator}
 * with String as type parameter
 */
@FunctionalInterface
public interface MessageInterceptor<T> extends PlaceholderReplacer<T> {

    static String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * Similar to {@link PlaceholderReplacer} but receives
     * the original text and not a placeholder.
     */
    @Override
    @NotNull
    String replace(InterceptContext<T> context, String placeholder);

    @Override
    default String[] getPlaceholders() {
        return EMPTY_STRING_ARRAY;
    }

}
