package me.yushust.message.specific;

import org.jetbrains.annotations.Nullable;

/**
 * A functional interface that resolves an entity using
 * a {@code T} instance
 * Example:
 * <code>
 * class IdEntityResolver implements EntityResolver<Entity, UUID> {
 * <p>
 * private EntityStore store = ...;
 * <p>
 * public Entity resolve(UUID id) {
 * return store.find(id);
 * }
 * }
 * </code>
 *
 * @param <E> The entity type
 * @param <T> The resolvable type
 */
@FunctionalInterface
public interface EntityResolver<E, T> {

  /**
   * Resolves an entity using a {@code T} instance.
   *
   * @param object The resolving object
   * @return The resolved entity
   */
  @Nullable
  E resolve(T object);

}
