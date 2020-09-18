package me.yushust.message.generic;

import me.yushust.message.EntityResolver;
import me.yushust.message.util.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a registry of entity resolvers
 * @param <E> The entity type
 */
public class EntityResolverRegistry<E> {

  private final Map<Class<?>, EntityResolver<E, ?>> resolvers
      = new HashMap<>();

  /**
   * Finds an already registered {@link EntityResolver} and
   * returns it wrapped with {@link Optional}. If the entity resolver
   * isn't present, returns an empty {@linkplain Optional}
   * @param resolvableType The resolvable type
   * @return The entity resolver
   */
  public <T> Optional<EntityResolver<E, T>> findResolver(Class<T> resolvableType) {

    Validate.notNull(resolvableType, "resolvableType");
    EntityResolver<E, ?> resolver = resolvers.get(resolvableType);

    // it's safe, the map is modified only by
    // EntityResolverRegistry#addResolver that
    // adds the resolver using a generic method
    @SuppressWarnings("unchecked")
    EntityResolver<E, T> castedResolver = (EntityResolver<E, T>) resolver;
    return Optional.of(castedResolver);
  }

  /**
   * Registers an entity resolver to the backing map
   * @param resolvableType The resolvable type
   * @param resolver The resolver
   */
  public <T> void addResolver(Class<T> resolvableType, EntityResolver<E, T> resolver) {
    Validate.notNull(resolvableType, "resolvableType");
    Validate.notNull(resolver, "resolver");
    resolvers.put(resolvableType, resolver);
  }

}
