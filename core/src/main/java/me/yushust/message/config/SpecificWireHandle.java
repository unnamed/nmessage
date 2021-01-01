package me.yushust.message.config;

import me.yushust.message.specific.EntityResolver;
import me.yushust.message.specific.LanguageProvider;
import me.yushust.message.specific.Messenger;
import me.yushust.message.specific.PlaceholderProvider;
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
    Validate.notNull(resolvedClass, "resolvedClass");
    Validate.notNull(resolver, "resolver");
    wiringContainer.addResolver(resolvedClass, resolver);
    return this;
  }

  public SpecificWireHandle<E> setLinguist(
      LanguageProvider<E> linguist
  ) {
    Validate.notNull(linguist, "linguist");
    wiringContainer.setLinguist(entityType, linguist);
    return this;
  }

  public SpecificWireHandle<E> addProvider(
      String identifier,
      PlaceholderProvider<E> provider
  ) {
    Validate.notNull(identifier, "identifier");
    Validate.notNull(provider, "provider");
    wiringContainer.registerProvider(identifier, entityType, provider);
    return this;
  }

  public SpecificWireHandle<E> setMessenger(
      Messenger<E> messenger
  ) {
    Validate.notNull(messenger, "messenger");
    wiringContainer.setMessenger(entityType, messenger);
    return this;
  }

}
