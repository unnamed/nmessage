package me.yushust.message.config;

import me.yushust.message.ext.ReferencePlaceholderProvider;
import me.yushust.message.format.MessageInterceptor;
import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.impl.TypeSpecificPlaceholderProvider;
import me.yushust.message.language.Linguist;
import me.yushust.message.resolve.EntityResolver;
import me.yushust.message.send.MessageSender;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class that holds all the configurations for
 * entities with a pretty user-friendly API
 */
public final class ConfigurationHandle {

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

  /** The initial delimiter to recognise placeholders */
  private String startDelimiter = "%";

  /** The final delimiter to recognise placeholders */
  private String endDelimiter = "%";

  public ConfigurationHandle() {
    providers.put(
      "path",
      new TypeSpecificPlaceholderProvider<>(
        Object.class,
        new ReferencePlaceholderProvider<>()
      )
    );
  }

  /**
   * Specifies the delimiters used by the placeholder
   * replacer to recognise placeholders
   * @param start The initial delimiter
   * @param end The end delimiter
   * @return A reference to {@code this}, for a fluent api
   */
  public ConfigurationHandle delimiting(String start, String end) {
    this.startDelimiter = Validate.isNotEmpty(start);
    this.endDelimiter = Validate.isNotEmpty(end);
    return this;
  }

  /**
   * Registers the given {@code interceptor} to the
   * interceptors list
   * @return A reference to {@code this}, for a fluent api
   */
  @Contract("null -> fail; _ -> this")
  public ConfigurationHandle addInterceptor(MessageInterceptor interceptor) {
    Validate.isNotNull(interceptor, "interceptor");
    interceptors.add(interceptor);
    return this;
  }

  /**
   * Creates a new specific configuration handle for
   * the given {@code entityType}. Used to avoid some
   * repeated code when registering many handlers
   * @param entityType The specifying entity type
   * @param <E> The entity type
   * @return The created specific configuration handle
   */
  @Contract("null -> fail; _ -> new")
  public <E> Specific<E> specify(Class<E> entityType) {
    Validate.isNotNull(entityType, "entityType");
    return new Specific<>(entityType);
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

    Class<?> type = entityType;

    while (type != Object.class && type != null) {
      HandlerPack<?> handlerPack = handlers.get(type);
      if (handlerPack != null) {
        compatibleSupertypes.put(entityType, type);
        return handlerPack;
      }
      for (Class<?> interfaceType : type.getInterfaces()) {
        handlerPack = handlers.get(interfaceType);
        if (handlerPack != null) {
          compatibleSupertypes.put(entityType, interfaceType);
          return handlerPack;
        }
        for (Class<?> superInterface : interfaceType.getInterfaces()) {
          handlerPack = handlers.get(superInterface);
          if (handlerPack != null) {
            compatibleSupertypes.put(entityType, superInterface);
            return handlerPack;
          }
        }
      }
      type = type.getSuperclass();
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  private <E> HandlerPack<E> getHandlersOrCreate(Class<?> entityType) {

    HandlerPack<?> handlerPack = handlers.get(entityType);

    if (handlerPack == null) {
      handlerPack = new HandlerPack<>();
      compatibleSupertypes.put(entityType, entityType);
      handlers.put(entityType, handlerPack);
    }

    return (HandlerPack<E>) handlerPack;
  }
  //#endregion

  /**
   * Installs the specified {@code modules} by calling
   * {@link ConfigurationModule#configure} passing the
   * {@code this} reference as argument
   * @param modules The installed modules
   * @return Reference to {@code this}, for a fluent api
   */
  @Contract("null -> fail")
  public ConfigurationHandle install(ConfigurationModule... modules) {
    for (ConfigurationModule module : modules) {
      module.configure(this);
    }
    return this;
  }

  /** Returns the initial delimiter for detecting placeholders */
  public String getStartDelimiter() {
    return startDelimiter;
  }

  /** Returns the final delimiter for detecting placeholders */
  public String getEndDelimiter() {
    return endDelimiter;
  }

  private static class HandlerPack<E> {

    // resolves this type to other type. Commonly when
    // this field isn't null, other properties are null
    private EntityResolver<?, E> resolver;

    private Linguist<E> linguist;
    private MessageSender<E> sender;

  }

  /**
   * It's a {@link ConfigurationHandle} linked
   * to an entity type
   * @param <E> The linked entity type
   */
  public class Specific<E> {

    /** The entity type for this configuration handle */
    private final Class<E> entityType;

    private Specific(Class<E> entityType) {
      this.entityType = entityType;
    }

    /**
     * Adds a resolver from the specified {@code resolvedClass}
     * to the entity class linked to this class.
     */
    public <R> Specific<E> resolveFrom(
        Class<R> resolvedClass,
        EntityResolver<E, R> resolver
    ) {
      Validate.isNotNull(resolvedClass, "resolvedClass");
      Validate.isNotNull(resolver, "resolver");
      HandlerPack<R> handlerPack = getHandlersOrCreate(resolvedClass);
      handlerPack.resolver = resolver;
      return this;
    }

    /**
     * Sets the language provider (linguist) for the
     * entity class linked to this class
     */
    public Specific<E> setLinguist(
        Linguist<E> linguist
    ) {
      Validate.isNotNull(linguist, "linguist");
      HandlerPack<E> handlerPack = getHandlersOrCreate(entityType);
      handlerPack.linguist = linguist;
      return this;
    }

    /**
     * Adds a placeholder provider with same entity type
     * as the linked to this class.
     */
    public Specific<E> addProvider(
        String identifier,
        PlaceholderProvider<E> provider
    ) {
      Validate.isNotNull(identifier, "identifier");
      Validate.isNotNull(provider, "provider");

      TypeSpecificPlaceholderProvider<?> resolvedProvider =
        new TypeSpecificPlaceholderProvider<>(entityType, provider);
      providers.put(identifier, resolvedProvider);
      return this;
    }

    /**
     * Sets the message sender for the entity type
     * linked to this class
     */
    public Specific<E> setMessageSender(
        MessageSender<E> sender
    ) {
      Validate.isNotNull(sender, "sender");
      HandlerPack<E> handlerPack = getHandlersOrCreate(entityType);
      handlerPack.sender = sender;
      return this;
    }

  }
}
