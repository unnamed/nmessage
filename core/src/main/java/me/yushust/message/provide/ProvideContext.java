package me.yushust.message.provide;

import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.util.Validate;

import java.util.*;

public final class ProvideContext<T> {

  private final Set<PlaceholderProvider<?>> ignoredProviders;
  private final Collection<String> linkedPaths;
  private final T entity;
  private final Set<Object> jitEntities;

  private ProvideContext(Set<PlaceholderProvider<?>> ignoredProviders, Collection<String> linkedPaths,
                         T entity, Set<Object> jitEntities) {
    this.ignoredProviders = ignoredProviders;
    this.linkedPaths = linkedPaths;
    this.entity = entity;
    this.jitEntities = jitEntities;
  }

  public ProvideContext(T entity, Object... jitEntities) {
    this(new HashSet<>(), new HashSet<>(), entity,
        jitEntities.length == 0 ? Collections.emptySet() : new HashSet<>(Arrays.asList(jitEntities)));
  }

  public T getEntity() {
    return entity;
  }

  public Collection<String> getLinkedPaths() {
    return linkedPaths;
  }

  public Set<Object> getJitEntities() {
    return jitEntities;
  }

  public boolean ignores(PlaceholderProvider<?> provider) {
    Validate.notNull(provider, "provider");
    return ignoredProviders.contains(provider);
  }

  public ProvideContext<T> ignore(PlaceholderProvider<?> provider) {
    Validate.notNull(provider, "provider");
    ignoredProviders.add(provider);
    return this;
  }

  public ProvideContext<T> stopIgnoring(PlaceholderProvider<?> provider) {
    Validate.notNull(provider, "provider");
    ignoredProviders.remove(provider);
    return this;
  }

  public ProvideContext<T> copy() {
    return new ProvideContext<>(new HashSet<>(ignoredProviders),
        new HashSet<>(linkedPaths), entity, new HashSet<>(jitEntities));
  }

}
