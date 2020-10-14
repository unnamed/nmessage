package me.yushust.message.test;

import me.yushust.message.MessageRepository;
import me.yushust.message.PlaceholderProvider;
import me.yushust.message.ProviderIdentifier;
import org.jetbrains.annotations.Nullable;

/**
 * Stupid, but it's for testing
 */
@ProviderIdentifier("object")
public class HashCodePlaceholderProvider implements PlaceholderProvider<ConsoleEntity> {

  @Override
  @Nullable
  public String replace(MessageRepository repository, ConsoleEntity entity, String parameters) {

    if (parameters.equals("hashcode")) {
      return String.valueOf(entity.hashCode());
    }

    return null;
  }
}
