package me.yushust.message.internal;

import me.yushust.message.*;
import me.yushust.message.mode.Mode;
import me.yushust.message.specific.EntityResolver;
import me.yushust.message.specific.LanguageProvider;
import me.yushust.message.specific.Messenger;
import me.yushust.message.specific.PlaceholderProvider;
import me.yushust.message.strategy.Strategy;
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
  char startDelimiter = '%';
  char endDelimiter = '%';
  FormatterRegistry<E> formatterRegistry;
  LanguageProvider<E> languageProvider = LanguageProvider.dummy();
  Messenger<E> messenger = Messenger.dummy();
  EntityResolverRegistry<E> resolverRegistry = new EntityResolverRegistry<>();
  Class<?> modeType;
  Mode defaultMode;

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

  public MessageHandlerBuilder<E> addProvider(String identifier, PlaceholderProvider<E> provider) {
    formatterRegistry.registerProvider(identifier, provider);
    return this;
  }

  public final <M extends Mode> MessageHandlerBuilder<E> setModes(Class<M> modeType, M[] modes) {
    Validate.notNull(modes, "modes");
    Validate.argument(modes.length > 0, "An empty array was provided!");
    this.modeType = Validate.notNull(modeType, "modeType");
    for (M mode : modes) {
      if (mode.isDefault()) {
        this.defaultMode = mode;
        return this;
      }
    }
    throw new IllegalArgumentException("Array of modes doesn't contain the default mode!");
  }

  public <O> MessageHandlerBuilder<E> addExternalProvider(
      String identifier,
      Class<O> entityType,
      PlaceholderProvider<O> provider
  ) {
    formatterRegistry.registerProvider(identifier, entityType, provider);
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

  public MessageHandlerBuilder<E> setMessenger(Messenger<E> messenger) {
    this.messenger = Validate.notNull(messenger);
    return this;
  }

  public MessageHandlerBuilder<E> setDelimiters(char start, char end) {
    this.startDelimiter = start;
    this.endDelimiter = end;
    return this;
  }

  public MessageHandler<E> build() {
    return new MessageHandlerImpl<>(this);
  }

}
