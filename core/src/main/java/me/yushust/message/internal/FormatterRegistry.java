package me.yushust.message.internal;

import me.yushust.message.MessageInterceptor;
import me.yushust.message.specific.PlaceholderProvider;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a registry of formatters like
 * {@link PlaceholderProvider} and {@link MessageInterceptor}
 */
final class FormatterRegistry<E> {

  private final Map<String, TypeSpecificPlaceholderProvider<?>> providers = new ConcurrentHashMap<>();
  private final List<MessageInterceptor> interceptors = new LinkedList<>();
  private final Class<E> entityType;

  public FormatterRegistry(Class<E> entityType) {
    this.entityType = Validate.notNull(entityType, "entityType");
  }

  /**
   * Registers a message interceptor in
   * this {@link FormatterRegistry}. If the
   * interceptor is null, it throws a
   * {@link NullPointerException}
   */
  void registerInterceptor(MessageInterceptor interceptor) {
    Validate.notNull(interceptor, "interceptor");
    interceptors.add(interceptor);
  }

  /**
   * Registers a placeholder provider in this
   * {@link FormatterRegistry} using the specified
   * entity type. Used for Just-In-Time entities
   */
  <O> void registerProvider(String identifier, Class<O> providerEntityType, PlaceholderProvider<O> provider) {

    TypeSpecificPlaceholderProvider<?> resolvedProvider =
        new TypeSpecificPlaceholderProvider<>(providerEntityType, provider);

    providers.put(identifier, resolvedProvider);
  }

  /**
   * Registers a placeholder provider in the
   * {@linkplain FormatterRegistry} using the entity type.
   */
  void registerProvider(String identifier, PlaceholderProvider<E> provider) {
    registerProvider(identifier, entityType, provider);
  }

  /** Finds a provider using its identifier */
  @Nullable
  TypeSpecificPlaceholderProvider<?> getProvider(String identifier) {
    Validate.notEmpty(identifier);
    return providers.get(identifier.toLowerCase());
  }

  /** A list containing all the registered interceptors */
  List<MessageInterceptor> getInterceptors() {
    return interceptors;
  }

}
