package me.yushust.message.test;

import me.yushust.message.core.placeholder.PlaceholderProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Stupid, but it's for testing
 */
public class HashCodePlaceholderProvider extends PlaceholderProvider<ConsoleEntity> {

    public HashCodePlaceholderProvider() {
        super("object");
    }

    @Override
    public @Nullable String replace(ConsoleEntity entity, String parameters) {

        if (parameters.equals("hashcode")) {
            return String.valueOf(entity.hashCode());
        }

        return null;
    }
}
