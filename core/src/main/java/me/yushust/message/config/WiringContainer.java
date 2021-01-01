package me.yushust.message.config;

import me.yushust.message.MessageInterceptor;
import me.yushust.message.internal.EntityHandlerPack;
import me.yushust.message.internal.TypeSpecificPlaceholderProvider;
import me.yushust.message.specific.EntityResolver;
import me.yushust.message.specific.LanguageProvider;
import me.yushust.message.specific.Messenger;
import me.yushust.message.specific.PlaceholderProvider;
import me.yushust.message.util.ClassTreeMap;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WiringContainer {

  private final ClassTreeMap<EntityHandlerPack<?>> handlers
      = new ClassTreeMap<>();

  private final Map<String, TypeSpecificPlaceholderProvider<?>> providers
      = new ConcurrentHashMap<>();

  private final List<MessageInterceptor> interceptors
      = new LinkedList<>();

  public ClassTreeMap<EntityHandlerPack<?>> getHandlers() {
    return handlers;
  }

  public <E> EntityResolver<?, E> getResolver(Class<E> resolvedClass) {
    EntityHandlerPack<?> handlerPack = handlers.getExact(resolvedClass);
    if (handlerPack == null) {
      return null;
    } else {
      @SuppressWarnings("unchecked")
      EntityResolver<?, E> resolver =
          (EntityResolver<?, E>) handlerPack.getResolver();
      return resolver;
    }
  }

  /**
   * Registers a message interceptor in
   * this {@link WiringContainer}. If the
   * interceptor is null, it throws a
   * {@link NullPointerException}
   */
  public void registerInterceptor(MessageInterceptor interceptor) {
    Validate.notNull(interceptor, "interceptor");
    interceptors.add(interceptor);
  }

  /**
   * Registers a placeholder provider in this
   * {@link WiringContainer} using the specified
   * entity type. Used for Just-In-Time entities
   */
  public <E> void registerProvider(String identifier, Class<E> entityType, PlaceholderProvider<E> provider) {
    TypeSpecificPlaceholderProvider<?> resolvedProvider =
        new TypeSpecificPlaceholderProvider<>(entityType, provider);
    providers.put(identifier, resolvedProvider);
  }

  /** Finds a provider using its identifier */
  @Nullable
  public TypeSpecificPlaceholderProvider<?> getProvider(String identifier) {
    Validate.notEmpty(identifier);
    return providers.get(identifier.toLowerCase());
  }

  /** A list containing all the registered interceptors */
  public List<MessageInterceptor> getInterceptors() {
    return interceptors;
  }

  public <T> void addResolver(
      Class<T> resolvedType,
      EntityResolver<?, T> resolver
  ) {
    getHandlersOrCreate(resolvedType).setResolver(resolver);
  }

  public <E> void setLinguist(Class<E> entityType, LanguageProvider<E> linguist) {
    getHandlersOrCreate(entityType).setLanguageProvider(linguist);
  }

  public <E> void setMessenger(Class<E> entityType, Messenger<E> messenger) {
    getHandlersOrCreate(entityType).setMessenger(messenger);
  }

  private <E> EntityHandlerPack<E> getHandlersOrCreate(Class<E> entityType) {
    @SuppressWarnings("unchecked")
    EntityHandlerPack<E> handlerPack =
        (EntityHandlerPack<E>) handlers.getExact(entityType);
    if (handlerPack == null) {
      handlerPack = EntityHandlerPack.create();
      handlers.put(entityType, handlerPack);
    }
    return handlerPack;
  }

}
