package me.yushust.message.ext;

import me.yushust.message.track.ContextRepository;
import me.yushust.message.ProviderSettings;
import me.yushust.message.format.PlaceholderProvider;
import org.jetbrains.annotations.Nullable;

@ProviderSettings(
    requiresEntity = false
)
public final class ReferencePlaceholderProvider<E>
  implements PlaceholderProvider<E> {

  @Override
  public @Nullable String replace(
    ContextRepository handle,
    E entity,
    String path
  ) {
    return handle.get(path);
  }

}
