package me.yushust.message.internal;

import me.yushust.message.*;
import me.yushust.message.StringList;

import me.yushust.message.file.NodeFile;
import me.yushust.message.mode.Mode;
import me.yushust.message.specific.EntityResolver;
import me.yushust.message.specific.LanguageProvider;
import me.yushust.message.specific.Messenger;
import me.yushust.message.strategy.Notify;
import me.yushust.message.strategy.Strategy;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

public final class MessageHandlerImpl<E> implements MessageHandler<E> {

  private final EntityResolverRegistry<E> resolverRegistry;
  private final Class<E> entityType;
  private final PlaceholderReplacer replacer;
  private final FormatterRegistry<E> formatterRegistry;
  private final LanguageProvider<E> languageProvider;
  private final MessageRepository repository;
  private final Strategy strategy;
  private final Messenger<E> messenger;
  private final Class<?> modeType;
  private final Mode defaultMode;

  MessageHandlerImpl(MessageHandlerBuilder<E> builder) {
    this.resolverRegistry = builder.resolverRegistry;
    this.entityType = builder.entityType;
    this.formatterRegistry = builder.formatterRegistry;
    this.languageProvider = builder.languageProvider;
    this.repository = builder.messageRepository;
    this.strategy = repository.getStrategy();
    this.messenger = builder.messenger;
    this.replacer = new PlaceholderReplacer(this, builder.startDelimiter, builder.endDelimiter);
    this.modeType = builder.modeType;
    this.defaultMode = builder.defaultMode;
    formatterRegistry.registerProvider("path", new ReferencePlaceholderProvider<>());
  }

  public String format(
      InternalContext<E> context,
      String path,
      ReplacePack replacements,
      Object[] jitEntities,
      Object... orderedArgs
  ) {

    String language = context.getLanguage();
    String message = repository.getMessage(language, path);

    if (message == null) {
      return null;
    }
    message = replacements.replace(message);

    if (context.has(path)) {
      // Cycle linked messages
      strategy.warn(Notify.Warning.CYCLIC_LINKED_MESSAGES, context.export());
      return message;
    }

    context.push(path);
    message = replacer.replace(context, message, jitEntities);

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
    return message;
  }

  @Override
  public String format(E entity, String text) {
    return replacer.replace(
        new InternalContext<>(entity, languageOf(entity), this),
        text
    );
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
    InternalContext<E> context = new InternalContext<>(entity, language, this);
    return format(context, path, replacements, jitEntities, orderedArgs);
  }

  public StringList formatMany(
      InternalContext<E> context,
      String path,
      ReplacePack replacements,
      Object[] jitEntities,
      Object... orderedArgs
  ) {
    String language = context.getLanguage();
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
          line = replacer.replace(context, line, jitEntities);
          if (orderedArgs.length > 0) {
            line = String.format(line, orderedArgs);
          }
          return line;
        }
    );
    popAndCheckSame(context, path);
    return messages;
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
    InternalContext<E> context = new InternalContext<>(entity, language, this);
    return formatMany(context, path, replacements, jitEntities, orderedArgs);
  }

    @Override
    public NodeFile in(String lang) {
        return repository.in(lang);
    }

    @Nullable
  @SuppressWarnings({"unchecked", "rawtypes"})
  public E asEntity(Object resolvableEntity) {
    if (resolvableEntity == null) {
      return null;
    } else if (entityType.isInstance(resolvableEntity)) {
      return entityType.cast(resolvableEntity);
    }
    Class<?> clazz = resolvableEntity.getClass();
    EntityResolver resolver = resolverRegistry.findResolver(clazz);
    return resolver == null ? null : entityType.cast(resolver.resolve(resolvableEntity));
  }

  @Override
  public String getMessage(@Nullable String language, String messagePath) {
    language = orDefault(language);
    InternalContext<E> context = new InternalContext<>(null, language, this);
    context.push(messagePath);
    String message = repository.getMessage(language, messagePath);
    if (message != null) {
      message = replacer.replace(context, message);
    }
    popAndCheckSame(context, messagePath);
    return message;
  }

  @Override
  public StringList getMessages(@Nullable String language, String messagePath) {
    language = orDefault(language);
    InternalContext<E> context = new InternalContext<>(null, language, this);
    context.push(messagePath);
    StringList messages = repository.getMessages(language, messagePath);
    if (messages != null) {
      messages.replaceAll(message -> replacer.replace(context, message));
    }
    popAndCheckSame(context, messagePath);
    return messages;
  }

  private String orDefault(String language) {
    return language == null ? repository.getDefaultLanguage() : language;
  }

  @Override
  public Strategy getStrategy() {
    return repository.getStrategy();
  }

  @Override
  public String getDefaultLanguage() {
    return repository.getDefaultLanguage();
  }

  private String languageOf(E entity) {
    return entity == null ? getDefaultLanguage() : languageProvider.getLanguage(entity);
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

  @Override
  public LanguageProvider<E> getLanguageProvider() {
    return languageProvider;
  }

  @Override
  public Mode defaultMode() {
    return defaultMode;
  }

  @Override
  public void dispatch(
      Object entityOrEntities,
      String path,
      Mode mode,
      ReplacePack replacements,
      Object[] jitEntities,
      Object[] orderedArgs
  ) {
    if (mode == null) {
      mode = defaultMode;
    } else if (modeType != null && !modeType.isInstance(mode)) {
      throw new IllegalArgumentException("Invalid mode: " + mode);
    }
    if (entityOrEntities instanceof Iterable) {
      // supports Iterable<Iterable<Iterable<?>>>>. why? i don't know but it's supported
      for (Object resolvableEntity : (Iterable<?>) entityOrEntities) {
        dispatch(resolvableEntity, path, mode, replacements, jitEntities, orderedArgs);
      }
    } else {
      E entity = asEntity(entityOrEntities);
      String message = format(entity, path, replacements, jitEntities, orderedArgs);
      messenger.send(entity, mode, message);
    }
  }
}
