package me.yushust.message.internal;

import me.yushust.message.MessageRepository;
import me.yushust.message.PlaceholderProvider;
import me.yushust.message.util.Providers;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

public class ResolvedPlaceholderProvider<T> implements PlaceholderProvider<T> {

  private final Class<T> entityType;
  private final PlaceholderProvider<T> delegate;

  private final String identifier;
  private final boolean requiresEntity;

  public ResolvedPlaceholderProvider(Class<T> entityType, PlaceholderProvider<T> delegate) {
    this.entityType = Validate.notNull(entityType, "entityType");
    this.delegate = Validate.notNull(delegate, "delegate");
    this.identifier = Providers.getIdentifier(delegate);
    this.requiresEntity = Providers.requiresEntity(delegate);
  }

  String getIdentifier() {
    return identifier;
  }

  @Override
  @Nullable
  public String replace(MessageRepository repository, T entity, String parameters) {
    return delegate.replace(repository, entity, parameters);
  }

  @Nullable
  String replaceCasting(MessageRepository repository, Object entity, String parameters) {

    Validate.argument(accepts(entity), "The provided entity isn't acceptable by this provider");

    // it's safe, ResolvedPlaceholderProvider#accepts checks
    // if the provided object is an instance of the previously
    // specified entity type
    @SuppressWarnings("unchecked")
    T castedEntity = (T) entity;
    return replace(repository, castedEntity, parameters);
  }

  boolean accepts(Object entity) {
    return (entity == null && !requiresEntity) || entityType.isInstance(entity);
  }

}
