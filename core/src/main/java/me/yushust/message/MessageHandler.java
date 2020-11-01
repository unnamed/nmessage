package me.yushust.message;

import me.yushust.message.internal.FormattingContext;
import me.yushust.message.specific.LanguageProvider;
import me.yushust.message.internal.MessageHandlerBuilder;

public interface MessageHandler<E> extends MessageDispatcher, MessageRepository {

  String format(
      Object entity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities,
      Object... orderedArgs
  );

  StringList formatMany(
      Object entity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities,
      Object... orderedArgs
  );

  default String get(Object entity, String path, Object... jitEntities) {
    return format(entity, path, ReplacePack.EMPTY, jitEntities);
  }

  default String replacing(Object entity, String path, Object... replacements) {
    return format(entity, path, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY);
  }

  default String formatting(Object entity, String path, Object... args) {
    return format(entity, path, ReplacePack.EMPTY, EMPTY_OBJECT_ARRAY, args);
  }

  default StringList getMany(Object entity, String messagePath, Object... jitEntities) {
    return formatMany(entity, messagePath, ReplacePack.EMPTY, jitEntities);
  }

  default StringList replacingMany(Object entity, String messagePath, Object... replacements) {
    return formatMany(entity, messagePath, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY);
  }

  default StringList formattingMany(Object entity, String path, Object... args) {
    return formatMany(entity, path, ReplacePack.EMPTY, EMPTY_OBJECT_ARRAY, args);
  }

  @Deprecated
  default String getMessage(Object entity, String path, Object... jitEntities) {
    // compatibility method, this method isn't used
    // because "get" is more short xD
    return get(entity, path, jitEntities);
  }

  @Deprecated
  default StringList getMessages(Object entity, String messagePath, Object... jitEntities) {
    // compatibility method, this method isn't used
    // because "getMany" is more short xD
    return getMany(entity, messagePath, jitEntities);
  }

  FormattingContext<E> context();

  /**
   * Returns the {@link LanguageProvider +} for the specified entity class,
   * If a {@link LanguageProvider} was not given, returns a dummy {@link LanguageProvider}
   * @see LanguageProvider#dummy()
   */
  LanguageProvider<E> getLanguageProvider();

  static <T> MessageHandlerBuilder<T> builder(Class<T> entityType) {
    return new MessageHandlerBuilder<>(entityType);
  }

}
