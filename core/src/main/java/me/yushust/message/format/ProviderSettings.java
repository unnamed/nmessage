package me.yushust.message.format;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify settings
 * for a placeholder provider
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProviderSettings {

    /**
     * Specifies that the annotated provider
     * doesn't require an entity to provide the
     * values correctly
     */
    boolean requiresEntity() default true;

    /*
     * Specifies the identifiers when an entity
     * is provided as a external provider. So we can use
     * the principal entity type and the same type for an
     * external type.
     * TODO: Add this!
     */
    // String[] identifiersWhenExternal() default {};

}
