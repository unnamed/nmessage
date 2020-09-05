package me.yushust.message.placeholder.annotation;

import me.yushust.message.placeholder.PlaceholderProvider;

import java.lang.annotation.*;

/**
 * Specifies that the annotated
 * {@link PlaceholderProvider} doesn't
 * require an entity to function correctly
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptionalEntity {
}
