package me.yushust.message.internal;

import me.yushust.message.*;
import me.yushust.message.StringList;

import me.yushust.message.config.Specifier;
import me.yushust.message.config.WiringContainer;
import me.yushust.message.mode.Mode;
import me.yushust.message.specific.EntityResolver;
import me.yushust.message.specific.Linguist;
import me.yushust.message.specific.Messenger;
import me.yushust.message.strategy.Notify;
import me.yushust.message.strategy.Strategy;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class MessageHandlerImpl
    extends DelegatingMessageRepository
    implements MessageHandler {

  private final WiringContainer wiringContainer;
  private final MessageRepository repository;
  private final Strategy strategy;
  private final Class<?> modeType;
  private final Mode defaultMode;
  private final PlaceholderReplacer replacer;

  public MessageHandlerImpl(MessageRepository repository, Specifier... specifiers) {
    super(repository);
    WireHandleImpl wireHandle = new WireHandleImpl();
    for (Specifier specifier : specifiers) {
      specifier.configure(wireHandle);
    }
    this.wiringContainer = wireHandle.getWiringContainer();
    this.repository = repository;
    this.strategy = repository.getStrategy();
    this.modeType = wireHandle.getModesType();
    this.defaultMode = wireHandle.getDefaultMode();
    this.replacer = new PlaceholderReplacer(
        wiringContainer,
        wireHandle.getStartDelimiter(),
        wireHandle.getEndDelimiter()
    );
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
    message = replacer.format(context, message, jitEntities);

    /*
     * The String.format method is called after
     * placeholder replacement because the placeholder
     * delimiters can cause an exception by being
     * "special characters"
     */
    if (orderedArgs.length > 0) {
      message = String.format(message, orderedArgs);
    }

    context.popAndCheckSame(path);
    return message;
  }

  @Override
  public String format(Object entity, String text) {
    return replacer.format(
        new InternalContext(entity, languageOf(entity), this),
        text,
        EMPTY_OBJECT_ARRAY
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
    return format(makeContext(resolvableEntity), path, replacements, jitEntities, orderedArgs);
  }

  private InternalContext makeContext(Object resolvableEntity) {
    Object entity = asEntity(resolvableEntity);
    String language = languageOf(entity);
    return new InternalContext(entity, language, this);
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
          line = replacer.format(context, line, jitEntities);
          if (orderedArgs.length > 0) {
            line = String.format(line, orderedArgs);
          }
          return line;
        }
    );
    context.popAndCheckSame(path);
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
    return formatMany(makeContext(resolvableEntity), path, replacements, jitEntities, orderedArgs);
  }

  @Override
  public <T> Linguist<T> getLanguageProvider(Class<T> entityType) {
    EntityHandlerPack<?> handlerPack =
        wiringContainer.getHandlers().get(entityType);

    if (handlerPack == null) {
      return Linguist.dummy();
    }

    @SuppressWarnings("unchecked")
    Linguist<T> languageProvider =
        (Linguist<T>) handlerPack.getLanguageProvider();
    return languageProvider;
  }

  @Nullable
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Contract("!null -> !null")
  public Object asEntity(Object resolvableEntity) {
    if (resolvableEntity == null) {
      return null;
    }
    Class<?> clazz = resolvableEntity.getClass();
    EntityResolver resolver = wiringContainer.getResolver(clazz);
    if (resolver == null) {
      return resolvableEntity;
    } else {
      Object resolved = resolver.resolve(resolvableEntity);
      if (resolved == null) {
        return resolvableEntity;
      } else {
        return resolved;
      }
    }
  }

  @Override
  public String getMessage(@Nullable String language, String messagePath) {
    language = orDefault(language);
    InternalContext context = new InternalContext(null, language, this);
    context.push(messagePath);
    String message = repository.getMessage(language, messagePath);
    if (message != null) {
      message = replacer.format(context, message, EMPTY_OBJECT_ARRAY);
    }
    context.popAndCheckSame(messagePath);
    return message;
  }

  @Override
  public StringList getMessages(@Nullable String language, String messagePath) {
    language = orDefault(language);
    InternalContext context = new InternalContext(null, language, this);
    context.push(messagePath);
    StringList messages = repository.getMessages(language, messagePath);
    if (messages != null) {
      messages.replaceAll(message -> replacer.format(context, message, EMPTY_OBJECT_ARRAY));
    }
    context.popAndCheckSame(messagePath);
    return messages;
  }

  private String orDefault(String language) {
    return language == null ? repository.getDefaultLanguage() : language;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private String languageOf(Object entity) {
    if (entity == null) {
      return getDefaultLanguage();
    } else {
      Linguist languageProvider =
          getLanguageProvider(entity.getClass());

      if (languageProvider == null) {
        return getDefaultLanguage();
      } else {
        return orDefault(languageProvider.getLanguage(entity));
      }
    }
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
    Validate.notNull(entityOrEntities, "entityOrEntities");
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
      EntityHandlerPack<?> handlerPack = wiringContainer.getHandlers().get(entity.getClass());
      Validate.argument(handlerPack != null, "No handlers registered for " + entity.getClass());
      String message = format(entity, path, replacements, jitEntities, orderedArgs);
      Messenger messenger = handlerPack.getMessenger();
      Validate.argument(messenger != null, "No messenger specified for " + entity.getClass());
      messenger.send(entity, mode, message);
    }
  }
}
