package me.yushust.message.internal;

import me.yushust.message.MessageRepository;
import me.yushust.message.OptionalEntity;
import me.yushust.message.PlaceholderProvider;
import me.yushust.message.ProviderIdentifier;
import org.jetbrains.annotations.Nullable;

@ProviderIdentifier("path")
@OptionalEntity
public class ReferencePlaceholderProvider<E> implements PlaceholderProvider<E> {

  @Override
  public @Nullable String replace(MessageRepository repository, E entity, String parameters) {
    return repository.getMessage(parameters);
  }

}
