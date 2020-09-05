package me.yushust.message.test;

import me.yushust.message.MessageRepository;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.placeholder.annotation.ProviderIdentifier;
import org.jetbrains.annotations.Nullable;

/**
 * Stupid, but it's for testing
 */
@ProviderIdentifier("object")
public class HashCodePlaceholderProvider extends PlaceholderProvider<ConsoleEntity> {

    @Override
    protected @Nullable String replace(MessageRepository repository, ConsoleEntity entity, String parameters) {

        if (parameters.equals("hashcode")) {
            return String.valueOf(entity.hashCode());
        }

        return null;
    }
}
