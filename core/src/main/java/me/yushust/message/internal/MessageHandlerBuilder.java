package me.yushust.message.internal;

import me.yushust.message.*;
import me.yushust.message.util.Validate;

/**
 * A MessageProvider Builder, fluent API.
 * Used to create a MessageProvider
 *
 * @param <E> The entity type
 */
public final class MessageHandlerBuilder<E> {

  // optional values
  Class<E> entityType;
  PlaceholderBox delimiters = PlaceholderBox.DEFAULT;
  FormatterRegistry<E> formatterRegistry;
  LanguageProvider<E> languageProvider = LanguageProvider.dummy();
  MessageConsumer<E> messageConsumer = MessageConsumer.dummy();
  FailureListener failureListener = FailureListener.DUMMY;
  EntityResolverRegistry<E> resolverRegistry = new EntityResolverRegistry<>();

  // required value
  MessageRepository messageRepository;

  public MessageHandlerBuilder(Class<E> entityType) {
    this.formatterRegistry = new FormatterRegistry<>(entityType);
    this.entityType = entityType;
  }

  public <T> MessageHandlerBuilder<E> addResolver(Class<T> resolvableType, EntityResolver<E, T> resolver) {
    this.resolverRegistry.addResolver(resolvableType, resolver);
    return this;
  }

  public MessageHandlerBuilder<E> setRepository(MessageRepository messageRepository) {
    this.messageRepository = Validate.notNull(messageRepository);
    return this;
  }

  public MessageHandlerBuilder<E> addProvider(PlaceholderProvider<E> provider) {
    formatterRegistry.registerProvider(provider);
    return this;
  }

  public <O> MessageHandlerBuilder<E> addExternalProvider(Class<O> entityType, PlaceholderProvider<O> provider) {
    formatterRegistry.registerProvider(entityType, provider);
    return this;
  }

  public MessageHandlerBuilder<E> addInterceptor(MessageInterceptor interceptor) {
    formatterRegistry.registerInterceptor(interceptor);
    return this;
  }

  public MessageHandlerBuilder<E> setLanguageProvider(LanguageProvider<E> languageProvider) {
    this.languageProvider = Validate.notNull(languageProvider);
    return this;
  }

  public MessageHandlerBuilder<E> setMessageConsumer(MessageConsumer<E> messageConsumer) {
    this.messageConsumer = Validate.notNull(messageConsumer);
    return this;
  }

  public MessageHandlerBuilder<E> setDelimiters(char start, char end) {
    this.delimiters = new PlaceholderBox(start, end);
    return this;
  }

  public MessageHandlerBuilder<E> setFailureListener(FailureListener failureListener) {
    this.failureListener = Validate.notNull(failureListener);
    return this;
  }

  public MessageHandler<E> build() {
    return new MessageHandlerImpl<>(this);
  }

}
