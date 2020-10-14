package me.yushust.message.util;

import me.yushust.message.PlaceholderProvider;
import me.yushust.message.OptionalEntity;
import me.yushust.message.ProviderIdentifier;

/**
 * Collection of util methods for {@link PlaceholderProvider}s
 */
public final class Providers {

  private Providers() {
    throw new UnsupportedOperationException("This class couldn't be instantiated!");
  }

  /**
   * Checks for ProviderIdentifier annotation
   *
   * @param provider The provider that will be identified.
   * @return The real identifier.
   */
  public static String getIdentifier(PlaceholderProvider<?> provider) {

    Validate.notNull(provider, "provider");

    ProviderIdentifier idAnnotation = provider.getClass().getAnnotation(ProviderIdentifier.class);

    Validate.state(
        idAnnotation != null,
        "The provider " + provider + " doesn't implement getIdentifier() and "
            + " isn't annotated with ProviderIdentifier!"
    );

    return Validate.notEmpty(idAnnotation.value()).toLowerCase();
  }

  /**
   * Checks if the provider requires an entity to work.
   * How? it looks for a {@link OptionalEntity} annotation.
   *
   * @param provider The checking provider
   * @return False if the provider is annotated with
   * {@link OptionalEntity}
   */
  public static boolean requiresEntity(PlaceholderProvider<?> provider) {

    Validate.notNull(provider, "provider");

    return !provider.getClass()
        .isAnnotationPresent(OptionalEntity.class);
  }

}
