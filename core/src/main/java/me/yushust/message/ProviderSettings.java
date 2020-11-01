package me.yushust.message;

import java.lang.annotation.*;

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

}
