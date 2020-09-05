package me.yushust.message.util;

import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.placeholder.annotation.OptionalEntity;
import me.yushust.message.placeholder.annotation.ProviderIdentifier;

/**
 * Collection of util methods for {@link PlaceholderProvider}s
 */
public final class Providers {

    private Providers() {
        throw new UnsupportedOperationException("This class couldn't be instantiated!");
    }

    /**
     * Executes the {@link PlaceholderProvider#getIdentifier} method
     * in the specified provider, if it returns null, checks for
     * @param provider The provider that will be identified.
     * @return The real identifier.
     */
    public static String getIdentifier(PlaceholderProvider<?> provider) {

        Validate.notNull(provider, "provider");

        String identifier = provider.getIdentifier();

        if (identifier != null) {
            return Validate.notEmpty(identifier).toLowerCase();
        }

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
