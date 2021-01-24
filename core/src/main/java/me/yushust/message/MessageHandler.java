package me.yushust.message;

import me.yushust.message.config.Specifier;
import me.yushust.message.internal.MessageHandlerImpl;
import me.yushust.message.language.Linguist;
import me.yushust.message.util.StringList;

public interface MessageHandler extends MessageProvider {

  Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  String format(Object entity, String text);

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

  /**
   * Returns the {@link Linguist} for the specified entity,
   * If a {@link Linguist} was not given, returns a dummy {@link Linguist}
   * @see Linguist#dummy()
   */
  <T> Linguist<T> getLanguageProvider(Class<T> entityType);

  static MessageHandler create(
      MessageProvider repository,
      Specifier... specifiers
  ) {
    return new MessageHandlerImpl(repository, specifiers);
  }

}
