package me.yushust.message.specific;

import me.yushust.message.track.ContextRepository;
import me.yushust.message.MessageHandler;
import org.jetbrains.annotations.Nullable;

/**
 * A class that provides a value for the specified
 * parameters and the provided entity.
 * If {@link MessageHandler#getMessage} is called
 * in {@link PlaceholderProvider#replace}, this
 * {@link PlaceholderProvider} is ignored
 *
 * @param <E> The entity type
 */
public interface PlaceholderProvider<E> {

  /**
   * Returns a value corresponding to the specified
   * parameters using the properties of
   * the specified entity.
   */
  @Nullable
  Object replace(ContextRepository repository, E entity, String parameters);

}
