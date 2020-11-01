package me.yushust.message.internal;

import me.yushust.message.MessageHandler;
import me.yushust.message.ProviderSettings;
import me.yushust.message.specific.PlaceholderProvider;
import org.jetbrains.annotations.Nullable;

@ProviderSettings(
    requiresEntity = false
)
final class ReferencePlaceholderProvider<E> implements PlaceholderProvider<E> {

  @Override
  public @Nullable String replace(MessageHandler<E> handle, E entity, String parameters) {
    return handle.getMessage(parameters);
  }

}
