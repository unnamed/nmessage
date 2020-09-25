package me.yushust.message.intercept;

import me.yushust.message.generic.ResolvedPlaceholderProvider;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFormatterRegistry<E> implements FormatterRegistry<E> {

  private final Map<String, ResolvedPlaceholderProvider<?>> providers = new ConcurrentHashMap<>();
  private final List<MessageInterceptor> interceptors = new LinkedList<>();
  private final Class<E> entityType;

  public DefaultFormatterRegistry(Class<E> entityType) {
    this.entityType = Validate.notNull(entityType, "entityType");
  }

  @Override
  public Class<E> getEntityType() {
    return entityType;
  }

  @Override
  public FormatterRegistry<E> registerInterceptor(MessageInterceptor interceptor) {
    Validate.notNull(interceptor, "interceptor");
    interceptors.add(interceptor);
    return this;
  }

  @Override
  public <O> FormatterRegistry<E> registerProvider(Class<O> providerEntityType, PlaceholderProvider<O> provider) {

    ResolvedPlaceholderProvider<?> resolvedProvider =
        new ResolvedPlaceholderProvider<>(providerEntityType, provider);

    providers.put(resolvedProvider.getIdentifier(), resolvedProvider);
    return this;
  }

  @Override
  @Nullable
  public ResolvedPlaceholderProvider<?> getProvider(String identifier) {
    Validate.notEmpty(identifier);
    return providers.get(identifier.toLowerCase());
  }

  @Override
  public List<MessageInterceptor> getInterceptors() {
    return interceptors;
  }

}
