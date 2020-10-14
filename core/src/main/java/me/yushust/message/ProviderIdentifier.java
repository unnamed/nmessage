package me.yushust.message;

import java.lang.annotation.*;

/**
 * Specifies the identifier of the annotated
 * {@link PlaceholderProvider}, if the annotation
 * isn't present, and the method doesn't implement
 * the method {@link PlaceholderProvider#getIdentifier},
 * it results in a {@link IllegalStateException} thrown.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProviderIdentifier {

  /**
   * @return The identifier of the annotated
   * {@link PlaceholderProvider}
   */
  String value();

}
