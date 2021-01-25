package me.yushust.message.config;

import me.yushust.message.language.Linguist;
import me.yushust.message.send.MessageSender;
import me.yushust.message.resolve.EntityResolver;
import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.util.Validate;

public class SpecificWireHandle<E> {

  private final ConfigurationContainer configurationContainer;
  private final Class<E> entityType;

  public SpecificWireHandle(
      ConfigurationContainer configurationContainer,
      Class<E> entityType
  ) {
    this.configurationContainer = configurationContainer;
    this.entityType = entityType;
  }

  public <R> SpecificWireHandle<E> resolveFrom(
      Class<R> resolvedClass,
      EntityResolver<E, R> resolver
  ) {
    Validate.isNotNull(resolvedClass, "resolvedClass");
    Validate.isNotNull(resolver, "resolver");
    configurationContainer.addResolver(resolvedClass, resolver);
    return this;
  }

  public SpecificWireHandle<E> setLinguist(
      Linguist<E> linguist
  ) {
    Validate.isNotNull(linguist, "linguist");
    configurationContainer.setLinguist(entityType, linguist);
    return this;
  }

  public SpecificWireHandle<E> addProvider(
      String identifier,
      PlaceholderProvider<E> provider
  ) {
    Validate.isNotNull(identifier, "identifier");
    Validate.isNotNull(provider, "provider");
    configurationContainer.registerProvider(identifier, entityType, provider);
    return this;
  }

  public SpecificWireHandle<E> setMessageSender(
      MessageSender<E> sender
  ) {
    Validate.isNotNull(sender, "sender");
    configurationContainer.setMessageSender(entityType, sender);
    return this;
  }

}
