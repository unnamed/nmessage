package me.yushust.message.internal;

import me.yushust.message.MessageHandler;
import me.yushust.message.ProviderSettings;
import me.yushust.message.specific.PlaceholderProvider;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of {@link PlaceholderProvider} that adds
 * more information about the provider, like the entity
 * type and its {@link ProviderSettings}
 * @param <E> The entity type
 */
final class TypeSpecificPlaceholderProvider<E> implements PlaceholderProvider<E> {

  private final Class<E> type;
  private final PlaceholderProvider<E> delegate;
  private final ProviderSettings settings;

  TypeSpecificPlaceholderProvider(Class<E> type, PlaceholderProvider<E> delegate) {
    this.type = Validate.notNull(type, "type");
    this.delegate = Validate.notNull(delegate, "delegate");
    this.settings = computeSettings();
  }

  @Override
  public @Nullable Object replace(MessageHandler<E> handler, E entity, String parameters) {
    return delegate.replace(handler, entity, parameters);
  }

  /** Unchecked method for replacement */
  String replaceUnchecked(MessageHandler<?> handler, Object entity, String parameters) {
    @SuppressWarnings("unchecked")
    E castedEntity = (E) entity;
    @SuppressWarnings("unchecked")
    MessageHandler<E> castedHandler = (MessageHandler<E>) handler;
    Object value = delegate.replace(castedHandler, castedEntity, parameters);
    return value == null ? null : value.toString();
  }

  /**
   * Checks if the given object is compatible as entity
   * for this placeholder provider
   */
  boolean isCompatible(Object object) {
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
