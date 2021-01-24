package me.yushust.message.config;

import me.yushust.message.language.Linguist;
import me.yushust.message.send.MessageSender;
import me.yushust.message.resolve.EntityResolver;
import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.util.Validate;

public class SpecificWireHandle<E> {

  private final WiringContainer wiringContainer;
  private final Class<E> entityType;

  public SpecificWireHandle(
      WiringContainer wiringContainer,
      Class<E> entityType
  ) {
    this.wiringContainer = wiringContainer;
    this.entityType = entityType;
  }

  public <R> SpecificWireHandle<E> resolveFrom(
      Class<R> resolvedClass,
      EntityResolver<E, R> resolver
  ) {
    Validate.isNotNull(resolvedClass, "resolvedClass");
    Validate.isNotNull(resolver, "resolver");
    wiringContainer.addResolver(resolvedClass, resolver);
    return this;
  }

  public SpecificWireHandle<E> setLinguist(
      Linguist<E> linguist
  ) {
    Validate.isNotNull(linguist, "linguist");
    wiringContainer.setLinguist(entityType, linguist);
    return this;
  }

  public SpecificWireHandle<E> addProvider(
      String identifier,
      PlaceholderProvider<E> provider
  ) {
    Validate.isNotNull(identifier, "identifier");
    Validate.isNotNull(provider, "provider");
    wiringContainer.registerProvider(identifier, entityType, provider);
    return this;
  }

  public SpecificWireHandle<E> setMessageSender(
      MessageSender<E> sender
  ) {
    Validate.isNotNull(sender, "sender");
    wiringContainer.setMessageSender(entityType, sender);
    return this;
  }

}
