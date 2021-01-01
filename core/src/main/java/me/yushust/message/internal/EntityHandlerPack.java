package me.yushust.message.internal;

import me.yushust.message.specific.EntityResolver;
import me.yushust.message.specific.LanguageProvider;
import me.yushust.message.specific.Messenger;

public class EntityHandlerPack<E> {

  // resolves this type to other type. Commonly when
  // this field isn't null, other properties are null
  private EntityResolver<?, E> resolver;

  private LanguageProvider<E> languageProvider;
  private Messenger<E> messenger;

  private EntityHandlerPack() {
  }

  public void setResolver(EntityResolver<?, E> resolver) {
    this.resolver = resolver;
  }

  public void setLanguageProvider(LanguageProvider<E> languageProvider) {
    this.languageProvider = languageProvider;
  }

  public EntityHandlerPack<E> setMessenger(Messenger<E> messenger) {
    this.messenger = messenger;
    return this;
  }

  public EntityResolver<?, E> getResolver() {
    return resolver;
  }

  public LanguageProvider<E> getLanguageProvider() {
    return languageProvider;
  }

  public Messenger<E> getMessenger() {
    return messenger;
  }

  public static <E> EntityHandlerPack<E> create() {
    return new EntityHandlerPack<>();
  }

}
