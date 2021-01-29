package me.yushust.message.config;

import me.yushust.message.ext.ReferencePlaceholderProvider;
import me.yushust.message.format.MessageInterceptor;
import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.impl.TypeSpecificPlaceholderProvider;
import me.yushust.message.language.Linguist;
import me.yushust.message.resolve.EntityResolver;
import me.yushust.message.send.MessageSender;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class that holds all the configurations for
 * entities, this class isn't designed to be
 * used by the final users
 */
public class ConfigurationContainer {

  /**
   * Map containing the cached compatible supertypes,
   * avoiding checking for all super-types when a
   * handler pack is required.
   */
  private final Map<Class<?>, Class<?>> compatibleSupertypes
    = new HashMap<>();

  /**
   * Map containing all the handlers for specific
   * entity types used as keys.
   */
  private final Map<Class<?>, HandlerPack<?>> handlers
    = new HashMap<>();

  /** Registry of placeholder providers by its identifier */
  private final Map<String, TypeSpecificPlaceholderProvider<?>> providers
    = new HashMap<>();

  /** List containing all the message interceptors */
  private final List<MessageInterceptor> interceptors
    = new LinkedList<>();

  public ConfigurationContainer() {
    registerProvider(
      "path",
      Object.class,
      new ReferencePlaceholderProvider<>()
    );
  }

  //#region Getters
  /** Gets the {@link MessageSender} registered for the given {@code clazz} */
  public MessageSender<?> getSender(Class<?> clazz) {
    HandlerPack<?> handlerPack = getHandlers(clazz);
    return handlerPack == null ? null : handlerPack.sender;
  }

  /** Gets the {@link Linguist} registered for the given {@code clazz} */
  public Linguist<?> getLinguist(Class<?> clazz) {
    HandlerPack<?> handlerPack = getHandlers(clazz);
    return handlerPack == null ? null : handlerPack.linguist;
  }

  /** Gets the {@link EntityResolver} for to the given {@code clazz} */
  public EntityResolver<?, ?> getResolver(Class<?> clazz) {
    HandlerPack<?> handlerPack = handlers.get(clazz);
    return handlerPack == null ? null : handlerPack.resolver;
  }

  /** Finds a provider using its identifier */
  @Nullable
  public TypeSpecificPlaceholderProvider<?> getProvider(String identifier) {
    Validate.isNotEmpty(identifier);
    return providers.get(identifier.toLowerCase());
  }
  //#endregion

  //#region Setters
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
   * entity type.
   */
  public <E> void registerProvider(
    String identifier,
    Class<E> entityType,
    PlaceholderProvider<E> provider
  ) {
    TypeSpecificPlaceholderProvider<?> resolvedProvider =
      new TypeSpecificPlaceholderProvider<>(entityType, provider);
    providers.put(identifier, resolvedProvider);
  }

  /**
   * Adds a resolver, the given {@code resolvedType}
   * is transformed to another type
   */
  public <T> void setResolver(
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
  //#endregion

  //#region Handler methods
  /**
   * Executes all the {@link MessageInterceptor} using
   * the provided {@code text}
   */
  public String intercept(String text) {
    for (MessageInterceptor interceptor : interceptors) {
      text = interceptor.intercept(text);
    }
    return text;
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

    } while (type != null && type != Object.class);

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
  //#endregion

  private static class HandlerPack<E> {

    // resolves this type to other type. Commonly when
    // this field isn't null, other properties are null
    private EntityResolver<?, E> resolver;

    private Linguist<E> linguist;
    private MessageSender<E> sender;

  }

}
