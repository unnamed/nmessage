package me.yushust.message;

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
   *
   * @param repository The contextual repository
   * @param entity     The entity
   * @param parameters The parameters
   * @return The modified text
   */
  @Nullable
  String replace(MessageRepository repository, E entity, String parameters);

}
