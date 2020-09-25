package me.yushust.message.intercept;

import me.yushust.message.generic.ResolvedPlaceholderProvider;
import me.yushust.message.placeholder.PlaceholderProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a registry of formatters like
 * {@link PlaceholderProvider} and {@link MessageInterceptor}
 */
public interface FormatterRegistry<E> {

  /**
   * @return The entity type
   */
  Class<E> getEntityType();

  /**
   * Registers a message interceptor in
   * this {@link FormatterRegistry}. If the
   * interceptor is null, it throws a
   * {@link NullPointerException}
   *
   * @param interceptor The interceptor
   * @return The formatter registry, for a fluent api
   */
  FormatterRegistry<E> registerInterceptor(MessageInterceptor interceptor);

  /**
   * Registers a placeholder provider in this
   * {@link FormatterRegistry} using the specified
   * entity type. Used for Just-In-Time entities
   *
   * @param provider The provider
   * @return The formatter registry, for a fluent api
   */
  <O> FormatterRegistry<E> registerProvider(Class<O> entityType, PlaceholderProvider<O> provider);

  /**
   * Registers a placeholder provider in the
   * {@linkplain FormatterRegistry} using the entity
   * type. {@link FormatterRegistry#getEntityType()}
   *
   * @param provider The provider
   * @return The formatter registry, for a fluent api
   */
  default FormatterRegistry<E> registerProvider(PlaceholderProvider<E> provider) {
    return registerProvider(getEntityType(), provider);
  }

  /**
   * Finds a provider using its identifier
   *
   * @param identifier The provider identifier
   * @return The placeholder provider
   */
  @Nullable
  ResolvedPlaceholderProvider<?> getProvider(String identifier);

  /**
   * @return An unmodifiable list containing the
   * registered interceptors.
   */
  List<MessageInterceptor> getInterceptors();

}
