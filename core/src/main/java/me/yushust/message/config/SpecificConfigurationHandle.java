package me.yushust.message.config;

import me.yushust.message.language.Linguist;
import me.yushust.message.send.MessageSender;
import me.yushust.message.resolve.EntityResolver;
import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.util.Validate;

/**
 * It's a {@link ConfigurationHandle} linked
 * to an entity type
 * @param <E> The linked entity type
 */
public class SpecificConfigurationHandle<E> {

  /**
   * It's the real configuration container, functions
   * are delegated to this method, passing the linked
   * {@link SpecificConfigurationHandle#entityType}
   */
  private final ConfigurationHandle configurationHandle;

  /** The entity type for this configuration handle */
  private final Class<E> entityType;

  public SpecificConfigurationHandle(
      ConfigurationHandle configurationHandle,
      Class<E> entityType
  ) {
    this.configurationHandle = configurationHandle;
    this.entityType = entityType;
  }

  /**
   * Adds a resolver from the specified {@code resolvedClass}
   * to the entity class linked to this class.
   */
  public <R> SpecificConfigurationHandle<E> resolveFrom(
      Class<R> resolvedClass,
      EntityResolver<E, R> resolver
  ) {
    Validate.isNotNull(resolvedClass, "resolvedClass");
    Validate.isNotNull(resolver, "resolver");
    configurationHandle.setResolver(resolvedClass, resolver);
    return this;
  }

  /**
   * Sets the language provider (linguist) for the
   * entity class linked to this class
   */
  public SpecificConfigurationHandle<E> setLinguist(
      Linguist<E> linguist
  ) {
    Validate.isNotNull(linguist, "linguist");
    configurationHandle.setLinguist(entityType, linguist);
    return this;
  }

  /**
   * Adds a placeholder provider with same entity type
   * as the linked to this class.
   */
  public SpecificConfigurationHandle<E> addProvider(
      String identifier,
      PlaceholderProvider<E> provider
  ) {
    Validate.isNotNull(identifier, "identifier");
    Validate.isNotNull(provider, "provider");
    configurationHandle.registerProvider(identifier, entityType, provider);
    return this;
  }

  /**
   * Sets the message sender for the entity type
   * linked to this class
   */
  public SpecificConfigurationHandle<E> setMessageSender(
      MessageSender<E> sender
  ) {
    Validate.isNotNull(sender, "sender");
    configurationHandle.setMessageSender(entityType, sender);
    return this;
  }

}
