package me.yushust.message.internal;

import me.yushust.message.language.Linguist;
import me.yushust.message.send.MessageSender;
import me.yushust.message.specific.EntityResolver;

public class EntityHandlerPack<E> {

  // resolves this type to other type. Commonly when
  // this field isn't null, other properties are null
  private EntityResolver<?, E> resolver;

  private Linguist<E> languageProvider;
  private MessageSender<E> sender;

  private EntityHandlerPack() {
  }

  public void setResolver(EntityResolver<?, E> resolver) {
    this.resolver = resolver;
  }

  public void setLanguageProvider(Linguist<E> languageProvider) {
    this.languageProvider = languageProvider;
  }

  public EntityHandlerPack<E> setMessageSender(MessageSender<E> sender) {
    this.sender = sender;
    return this;
  }

  public EntityResolver<?, E> getResolver() {
    return resolver;
  }

  public Linguist<E> getLanguageProvider() {
    return languageProvider;
  }

  public MessageSender<E> getMessageSender() {
    return sender;
  }

  public static <E> EntityHandlerPack<E> create() {
    return new EntityHandlerPack<>();
  }

}
