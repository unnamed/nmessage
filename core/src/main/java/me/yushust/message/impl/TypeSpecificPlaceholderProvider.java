package me.yushust.message.impl;

import me.yushust.message.track.ContextRepository;
import me.yushust.message.ProviderSettings;
import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of {@link PlaceholderProvider} that adds
 * more information about the provider, like the entity
 * type and its {@link ProviderSettings}
 * @param <E> The entity type
 */
public final class TypeSpecificPlaceholderProvider<E> implements PlaceholderProvider<E> {

  private final Class<E> type;
  private final PlaceholderProvider<E> delegate;
  private final ProviderSettings settings;

  public TypeSpecificPlaceholderProvider(Class<E> type, PlaceholderProvider<E> delegate) {
    this.type = Validate.isNotNull(type, "type");
    this.delegate = Validate.isNotNull(delegate, "delegate");
    this.settings = computeSettings();
  }

  @Override
  public @Nullable Object replace(ContextRepository handler, E entity, String parameters) {
    return delegate.replace(handler, entity, parameters);
  }

  /** Unchecked method for replacement */
  public String replaceUnchecked(ContextRepository handler, Object entity, String parameters) {
    @SuppressWarnings("unchecked")
    E castedEntity = (E) entity;
    Object value = delegate.replace(handler, castedEntity, parameters);
    return value == null ? null : value.toString();
  }

  /**
   * Checks if the given object is compatible as entity
   * for this placeholder provider
   */
  public boolean isCompatible(Object object) {
    return (object == null && !settings.requiresEntity())
        || type.isInstance(object);
  }

  @Override
  public String toString() {
    return delegate.toString() + " (entity " + type.getName() + ")";
  }

  private ProviderSettings computeSettings() {
    ProviderSettings settings = delegate.getClass()
        .getAnnotation(ProviderSettings.class);
    if (settings == null) {
      return DefaultSettingsHolder.class.getAnnotation(ProviderSettings.class);
    } else {
      return settings;
    }
  }

  /**
   * Class annotated with {@link ProviderSettings} using
   * the default values for it. It's used to avoid null
   * checks and just use a "dummy" instance of the annotation.
   */
  @ProviderSettings
  private static final class DefaultSettingsHolder {
  }

}
