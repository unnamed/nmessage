package me.yushust.message.internal;

import me.yushust.message.*;
import me.yushust.message.StringList;

import me.yushust.message.specific.EntityResolver;
import me.yushust.message.specific.LanguageProvider;
import me.yushust.message.specific.Messenger;
import me.yushust.message.strategy.Notify;
import me.yushust.message.strategy.Strategy;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

final class MessageHandlerImpl<E> implements MessageHandler<E> {

  private final ThreadLocal<FormattingContext<E>> contextThreadLocal =
      new ThreadLocal<>();

  private final EntityResolverRegistry<E> resolverRegistry;
  private final Class<E> entityType;
  private final PlaceholderReplacer replacer;
  private final FormatterRegistry<E> formatterRegistry;
  private final LanguageProvider<E> languageProvider;
  private final MessageRepository repository;
  private final Strategy strategy;
  private final Messenger<E> messenger;

  MessageHandlerImpl(MessageHandlerBuilder<E> builder) {
    this.resolverRegistry = builder.resolverRegistry;
    this.entityType = builder.entityType;
    this.formatterRegistry = builder.formatterRegistry;
    this.languageProvider = builder.languageProvider;
    this.repository = builder.messageRepository;
    this.strategy = builder.strategy;
    this.messenger = builder.messenger;
    this.replacer = new PlaceholderReplacer(this, builder.startDelimiter, builder.endDelimiter);
    formatterRegistry.registerProvider("path", (ctx, entity, param) -> ctx.getMessage(param));
  }

  @Override
  public String format(
      Object resolvableEntity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities,
      Object... orderedArgs
  ) {
    Validate.notNull(path, "path");
    E entity = asEntity(resolvableEntity);
    String language = languageOf(entity);
    FormattingContext<E> context = context();
    boolean initialStateWasNull = context == null;

    if (context == null) {
      // Context not present = first non-nested call
      context = new FormattingContext<>(entity, language);
    }

    String message = repository.getMessage(language, path);
    if (message != null) {
      for (ReplacePack.Entry entry : replacements.getEntries()) {
        message = message.replace(entry.getOldValue(), entry.getNewValue());
      }

      if (context.has(path)) {
        // Cycle linked messages
        strategy.warn(Notify.Warning.CYCLIC_LINKED_MESSAGES, context.export());
        return message;
      }

      context.push(path);
      message = replacer.replace(entity, message, jitEntities);

      /*
       * The String.format method is called after
       * placeholder replacement because the placeholder
       * delimiters can cause an exception by being
       * "special characters"
       */
      if (orderedArgs.length > 0) {
        message = String.format(message, orderedArgs);
      }

      popAndCheckSame(context, path);

      if (initialStateWasNull) {
        contextThreadLocal.remove();
      }
    }

    return message;
  }

  @Override
  public StringList formatMany(
      Object resolvableEntity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities,
      Object... orderedArgs
  ) {
    Validate.notNull(path, "path");
    E entity = asEntity(resolvableEntity);
    String language = languageOf(entity);
    FormattingContext<E> context = context();
    boolean initialStateWasNull = context == null;

    if (context == null) {
      context = new FormattingContext<>(entity, language);
    }

    StringList messages = repository.getMessages(language, path);

    for (ReplacePack.Entry entry : replacements.getEntries()) {
      messages.replace(entry.getOldValue(), entry.getNewValue());
    }

    context.push(path);

    messages.replaceAll(
        line -> {
          if (line == null) {
            return null;
          }
          line = replacer.replace(entity, line, jitEntities);
          if (orderedArgs.length > 0) {
            line = String.format(line, orderedArgs);
          }
          return line;
        }
    );
    popAndCheckSame(context, path);

    if (initialStateWasNull) {
      contextThreadLocal.remove();
    }
    return messages;
  }

  @Override
  public FormattingContext<E> context() {
    return contextThreadLocal.get();
  }

  @Nullable
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
  public String getMessage(@Nullable String language, String messagePath) {
    if (language == null) {
      FormattingContext<E> context = contextThreadLocal.get();
      if (context != null) {
        return get(context.getEntity(), messagePath);
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
      FormattingContext<E> context = contextThreadLocal.get();
      if (context != null) {
        return getMany(context.getEntity(), messagePath);
      }
    }
    return repository.getMessages(language, messagePath);
  }

  @Override
  public String getDefaultLanguage() {
    return repository.getDefaultLanguage();
  }

  private String languageOf(E entity) {
    return entity == null ? getDefaultLanguage() : languageProvider.getLanguage(entity);
  }

  private void popAndCheckSame(FormattingContext<E> context, String path) {
    // Illegal state, the path stack is now invalid!
    String obtained = context.pop();
    if (!path.equals(obtained)) {
      throw new IllegalStateException("Invalid path stack, the obtained path isn't "
          + "equals to the previously pushed path!\n    Expected: " + path
          + "\n    Obtained: " + obtained);
    }
  }

  Strategy getStrategy() {
    return strategy;
  }

  FormatterRegistry<E> getFormatterRegistry() {
    return formatterRegistry;
  }

  @Override
  public LanguageProvider<E> getLanguageProvider() {
    return languageProvider;
  }

  @Override
  public void dispatch(
      Object resolvableEntity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities,
      Object[] orderedArgs
  ) {
    E entity = asEntity(resolvableEntity);
    String message = format(entity, path, replacements, jitEntities, orderedArgs);
    messenger.send(entity, message);
  }

  @Override
  public void dispatch(
      Iterable<?> entities,
      String path,
      ReplacePack replacements,
      Object[] jitEntities,
      Object[] orderedArgs
  ) {
    for (Object resolvableEntity : entities) {
      dispatch(resolvableEntity, path, replacements, jitEntities, orderedArgs);
    }
  }
}
