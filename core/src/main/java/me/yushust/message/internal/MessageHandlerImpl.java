package me.yushust.message.internal;

import me.yushust.message.*;
import me.yushust.message.StringList;

import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class MessageHandlerImpl<E> implements MessageHandler<E> {

  private final ThreadLocal<InternalContext<E>> contextThreadLocal =
      new ThreadLocal<>();

  private final EntityResolverRegistry<E> resolverRegistry;
  private final Class<E> entityType;
  private final PlaceholderReplacer replacer;
  private final FormatterRegistry<E> formatterRegistry;
  private final LanguageProvider<E> languageProvider;
  private final MessageRepository repository;
  private final FailureListener failureListener;
  private final MessageConsumer<E> messageConsumer;

  MessageHandlerImpl(MessageHandlerBuilder<E> builder) {
    this.resolverRegistry = builder.resolverRegistry;
    this.entityType = builder.entityType;
    this.formatterRegistry = builder.formatterRegistry;
    this.languageProvider = builder.languageProvider;
    this.repository = builder.messageRepository;
    this.failureListener = builder.failureListener;
    this.messageConsumer = builder.messageConsumer;
    this.replacer = new PlaceholderReplacer(this, builder.delimiters);
    formatterRegistry.registerProvider(new ReferencePlaceholderProvider<>());
  }

  @Override
  public String getMessage(Object resolvableEntity, String path, Object... entities) {

    Validate.notNull(path, "path");
    boolean hasContext = hasContext();
    E entity = asEntity(resolvableEntity);
    InternalContext<E> context = enterContext(entity);

    String language = languageOf(entity);
    String message = repository.getMessage(language, path);

    if (message == null) {
      return null;
    }

    if (context.has(path)) {
      // Cycle linked messagaes
      failureListener.warn(Notify.Warning.CYCLIC_LINKED_MESSAGES, context.export());
      return message;
    }

    context.push(path);
    String formattedMessage = replacer.replace(entity, message, entities);
    popAndCheckSame(context, path);

    if (!hasContext) {
      clearContext();
    }

    return formattedMessage;
  }

  @Nullable
  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public E asEntity(Object resolvableEntity) {
    if (entityType.isInstance(resolvableEntity)) {
      return (E) resolvableEntity;
    }
    Class<?> clazz = resolvableEntity.getClass();
    EntityResolver resolver = resolverRegistry.findResolver(clazz);
    return resolver == null ? null : (E) resolver.resolve(resolvableEntity);
  }

  @Override
  public LanguageProvider<E> getLanguageProvider() {
    return languageProvider;
  }

  @Override
  public FailureListener getFailureListener() {
    return failureListener;
  }

  @Override
  public StringList getMessages(Object resolvableEntity, String path, Object... jitEntities) {

    Validate.notNull(path, "path");
    boolean hasContext = hasContext();
    E entity = asEntity(resolvableEntity);
    InternalContext<E> context = enterContext(entity);

    String language = languageOf(entity);
    StringList messages = repository.getMessages(language, path);
    context.push(path);

    messages.replaceAll(
        line -> {
          if (line == null) {
            return null;
          }
          return replacer.replace(entity, line, jitEntities);
        }
    );
    popAndCheckSame(context, path);
    if (!hasContext) {
      clearContext();
    }
    return messages;
  }

  // Helper methods start
  @Override
  public void sendMessage(Object resolvableEntity, String messagePath, Object... jitEntities) {
    E entity = asEntity(resolvableEntity);
    if (entity != null) {
      messageConsumer.sendMessage(
          entity,
          getMessage(
              entity,
              messagePath,
              jitEntities
          )
      );
    }
  }

  @Override
  public void sendMessage(Iterable<?> entities, String messagePath, Object... jitEntities) {
    for (Object resolvableEntity : entities) {
      sendMessage(resolvableEntity, messagePath, jitEntities);
    }
  }

  @Override
  public void sendMessages(Object resolvableEntity, String messagePath, Object... jitEntities) {
    E entity = asEntity(resolvableEntity);
    if (entity == null) {
      return;
    }
    StringList messages = getMessages(entity, messagePath, jitEntities);
    for (String message : messages) {
      messageConsumer.sendMessage(entity, message);
    }
  }

  @Override
  public void sendMessages(Iterable<?> entities, String messagePath, Object... jitEntities) {
    for (Object resolvableEntity : entities) {
      sendMessages(resolvableEntity, messagePath, jitEntities);
    }
  }
  // Helper methods end

  @Override
  public String getMessage(@Nullable String language, String messagePath) {
    if (language == null) {
      InternalContext<E> context = contextThreadLocal.get();
      if (context != null) {
        return getMessage(context.getEntity(), messagePath);
      }
    }
    String message = repository.getMessage(language, messagePath);
    if (message != null) {
      message = replacer.replace(null, message);
    }
    return message;
  }

  @Override
  public StringList getMessages(@Nullable String language, String messagePath) {
    if (language == null) {
      InternalContext<E> context = contextThreadLocal.get();
      if (context != null) {
        return getMessages(context.getEntity(), messagePath);
      }
    }
    return repository.getMessages(language, messagePath);
  }

  private String languageOf(E entity) {
    return entity == null ? null : languageProvider.getLanguage(entity);
  }

  private void popAndCheckSame(InternalContext<E> context, String path) {
    // Illegal state, the path stack is now invalid!
    String obtained = context.pop();
    if (!path.equals(obtained)) {
      throw new IllegalStateException("Invalid path stack, the obtained path isn't "
          + "equals to the previously pushed path!\n    Expected: " + path
          + "\n    Obtained: " + obtained);
    }
  }

  FormatterRegistry<E> getFormatterRegistry() {
    return formatterRegistry;
  }

  /** Determines if the context has been started for this thread */
  boolean hasContext() {
    return contextThreadLocal.get() != null;
  }

  /**
   * Returns the existent context or creates a
   * new one with the specified entity.
   */
  private InternalContext<E> enterContext(E entity) {
    InternalContext<E> context = contextThreadLocal.get();
    if (context == null) {
      /*
      * "the start", the creation of the context
      * indicates the first call to getMessage(...)
      */
      context = new InternalContext<>(entity);
      contextThreadLocal.set(context);
    }
    Validate.state(
        Objects.equals(context.getEntity(), entity),
        "Tried to enter context with a different entity! '"
            + entity + "' isn't '" + context.getEntity() + "'"
    );
    return context;
  }

  private void clearContext() {
    contextThreadLocal.remove();
  }

}
