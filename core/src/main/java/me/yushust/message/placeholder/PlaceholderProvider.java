package me.yushust.message.placeholder;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;
import me.yushust.message.placeholder.annotation.ProviderIdentifier;
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
   * Returns the placeholder provider identifier.
   * In case of returning null, the type annotation
   * {@link ProviderIdentifier} will be used.
   * If it isn't present, a {@link IllegalArgumentException}
   * will be thrown.
   *
   * @return The identifier
   */
  @Nullable
  default String getIdentifier() {
    return null;
  }

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
