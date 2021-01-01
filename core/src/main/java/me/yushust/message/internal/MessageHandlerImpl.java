package me.yushust.message.internal;

import me.yushust.message.*;
import me.yushust.message.StringList;

import me.yushust.message.config.Specifier;
import me.yushust.message.config.WiringContainer;
import me.yushust.message.file.NodeFile;
import me.yushust.message.mode.Mode;
import me.yushust.message.specific.EntityResolver;
import me.yushust.message.specific.LanguageProvider;
import me.yushust.message.specific.Messenger;
import me.yushust.message.strategy.Notify;
import me.yushust.message.strategy.Strategy;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

public final class MessageHandlerImpl implements MessageHandler {

  private final WiringContainer wiringContainer;

  private final PlaceholderReplacer replacer;
  private final MessageRepository repository;
  private final Strategy strategy;
  private final Class<?> modeType;
  private final Mode defaultMode;

  public MessageHandlerImpl(MessageRepository repository, Specifier... specifiers) {
    WireHandleImpl wireHandle = new WireHandleImpl();
    for (Specifier specifier : specifiers) {
      specifier.configure(wireHandle);
    }
    this.wiringContainer = wireHandle.getWiringContainer();
    this.repository = repository;
    this.strategy = repository.getStrategy();
    this.replacer = new PlaceholderReplacer(this, wireHandle.getStartDelimiter(), wireHandle.getEndDelimiter());
    this.modeType = wireHandle.getModesType();
    this.defaultMode = wireHandle.getDefaultMode();
    wiringContainer.registerProvider("path", Object.class, new ReferencePlaceholderProvider<>());
  }

  public String format(
      InternalContext context,
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
  public String format(Object entity, String text) {
    return replacer.replace(
        new InternalContext(entity, languageOf(entity), this),
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
    Object entity = asEntity(resolvableEntity);
    String language = languageOf(entity);
    InternalContext context = new InternalContext(entity, language, this);
    return format(context, path, replacements, jitEntities, orderedArgs);
  }

  public StringList formatMany(
      InternalContext context,
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
    Object entity = asEntity(resolvableEntity);
    String language = languageOf(entity);
    InternalContext context = new InternalContext(entity, language, this);
    return formatMany(context, path, replacements, jitEntities, orderedArgs);
  }

  @Override
  public <T> LanguageProvider<T> getLanguageProvider(Class<T> entityType) {
    EntityHandlerPack<?> handlerPack =
        wiringContainer.getHandlers().get(entityType);

    if (handlerPack == null) {
      return LanguageProvider.dummy();
    }

    @SuppressWarnings("unchecked")
    LanguageProvider<T> languageProvider =
        (LanguageProvider<T>) handlerPack.getLanguageProvider();
    return languageProvider;
  }

  @Override
  public NodeFile in(String lang) {
    return repository.in(lang);
  }

  @Nullable
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Object asEntity(Object resolvableEntity) {
    if (resolvableEntity == null) {
      return null;
    }
    Class<?> clazz = resolvableEntity.getClass();
    EntityResolver resolver = wiringContainer.getResolver(clazz);
    return resolver == null ? resolvableEntity : resolver.resolve(resolvableEntity);
  }

  @Override
  public String getMessage(@Nullable String language, String messagePath) {
    language = orDefault(language);
    InternalContext context = new InternalContext(null, language, this);
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
    InternalContext context = new InternalContext(null, language, this);
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

  @SuppressWarnings({"rawtypes", "unchecked"})
  private String languageOf(Object entity) {
    if (entity == null) {
      return getDefaultLanguage();
    } else {
      LanguageProvider languageProvider =
          getLanguageProvider(entity.getClass());

      if (languageProvider == null) {
        return getDefaultLanguage();
      } else {
        String language = languageProvider.getLanguage(entity);
        if (language == null) {
          return getDefaultLanguage();
        } else {
          return language;
        }
      }
    }
  }

  private void popAndCheckSame(InternalContext context, String path) {
    // Illegal state, the path stack is now invalid!
    String obtained = context.pop();
    if (!path.equals(obtained)) {
      throw new IllegalStateException("Invalid path stack, the obtained path isn't "
          + "equals to the previously pushed path!\n    Expected: " + path
          + "\n    Obtained: " + obtained);
    }
  }

  WiringContainer getWiringContainer() {
    return wiringContainer;
  }

  @Override
  public Mode defaultMode() {
    return defaultMode;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
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
      Object entity = asEntity(entityOrEntities);
      if (entity == null) {
        return;
      }
      EntityHandlerPack<?> handlerPack = wiringContainer.getHandlers().get(entity.getClass());
      if (handlerPack == null) {
        throw new IllegalArgumentException("No handlers registered for " + entity.getClass());
      }
      String message = format(entity, path, replacements, jitEntities, orderedArgs);
      Messenger messenger = handlerPack.getMessenger();
      if (messenger == null) {
        throw new IllegalArgumentException("No messenger specified for " + entity.getClass());
      }
      messenger.send(entity, mode, message);
    }
  }
}
