package me.yushust.message.config;

import me.yushust.message.format.MessageInterceptor;
import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.internal.TypeSpecificPlaceholderProvider;
import me.yushust.message.language.Linguist;
import me.yushust.message.resolve.EntityResolver;
import me.yushust.message.send.MessageSender;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConfigurationContainer {

  private final Map<Class<?>, Class<?>> compatibleSupertypes
    = new HashMap<>();

  private final Map<Class<?>, HandlerPack<?>> handlers
    = new HashMap<>();

  private final Map<String, TypeSpecificPlaceholderProvider<?>> providers
    = new HashMap<>();

  private final List<MessageInterceptor> interceptors
    = new LinkedList<>();

  public Linguist<?> getLinguist(Class<?> clazz) {
    HandlerPack<?> handlerPack = getHandlers(clazz);
    return handlerPack == null ? null : handlerPack.linguist;
  }

  public <E> EntityResolver<?, E> getResolver(Class<E> resolvedClass) {
    HandlerPack<?> handlerPack = handlers.get(resolvedClass);
    if (handlerPack == null) {
      return null;
    } else {
      @SuppressWarnings("unchecked")
      EntityResolver<?, E> resolver =
        (EntityResolver<?, E>) handlerPack.resolver;
      return resolver;
    }
  }

  public String intercept(String text) {
    for (MessageInterceptor interceptor : interceptors) {
      text = interceptor.intercept(text);
    }
    return text;
  }

  /**
   * Registers a message interceptor in
   * this {@link ConfigurationContainer}. If the
   * interceptor is null, it throws a
   * {@link NullPointerException}
   */
  public void registerInterceptor(MessageInterceptor interceptor) {
    Validate.isNotNull(interceptor, "interceptor");
    interceptors.add(interceptor);
  }

  /**
   * Registers a placeholder provider in this
   * {@link ConfigurationContainer} using the specified
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
    Validate.isNotEmpty(identifier);
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
    HandlerPack<T> handlerPack = getHandlersOrCreate(resolvedType);
    handlerPack.resolver = resolver;
  }

  public <E> void setLinguist(Class<E> entityType, Linguist<E> linguist) {
    HandlerPack<E> handlerPack = getHandlersOrCreate(entityType);
    handlerPack.linguist = linguist;
  }

  public <E> void setMessageSender(Class<E> entityType, MessageSender<E> sender) {
    HandlerPack<E> handlerPack = getHandlersOrCreate(entityType);
    handlerPack.sender = sender;
  }

  private HandlerPack<?> getHandlers(Class<?> entityType) {

    Class<?> cachedCompatibleType = compatibleSupertypes.get(entityType);

    if (cachedCompatibleType != null) {
      return handlers.get(cachedCompatibleType);
    }

    HandlerPack<?> handlerPack;
    Class<?> type = entityType;
    int nextInterface = -1;

    do {
      handlerPack = handlers.get(type);
      if (handlerPack != null) {
        compatibleSupertypes.put(entityType, type);
        return handlerPack;
      }
      Class<?>[] interfaces = type.getInterfaces();
      if (nextInterface == -1) {
        type = type.getSuperclass();
        if (interfaces.length > 0) {
          nextInterface = 0;
        }
      } else {
        type = interfaces[nextInterface];
        if (++nextInterface > interfaces.length) {
          nextInterface = -1;
        }
      }

    } while (type != Object.class);

    return null;
  }

  @SuppressWarnings("unchecked")
  private <E> HandlerPack<E> getHandlersOrCreate(Class<?> entityType) {

    HandlerPack<?> handlerPack = getHandlers(entityType);

    if (handlerPack == null) {
      handlerPack = new HandlerPack<>();
      compatibleSupertypes.put(entityType, entityType);
      handlers.put(entityType, handlerPack);
    }

    return (HandlerPack<E>) handlerPack;
  }

  private static class HandlerPack<E> {

    // resolves this type to other type. Commonly when
    // this field isn't null, other properties are null
    private EntityResolver<?, E> resolver;

    private Linguist<E> linguist;
    private MessageSender<E> sender;

  }

}
