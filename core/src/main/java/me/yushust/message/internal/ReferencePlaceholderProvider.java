package me.yushust.message.internal;

import me.yushust.message.track.ContextRepository;
import me.yushust.message.ProviderSettings;
import me.yushust.message.format.PlaceholderProvider;
import org.jetbrains.annotations.Nullable;

@ProviderSettings(
    requiresEntity = false
)
final class ReferencePlaceholderProvider<E> implements PlaceholderProvider<E> {

  @Override
  public @Nullable String replace(ContextRepository handle, E entity, String parameters) {
    return handle.getMessage(parameters);
  }

}
