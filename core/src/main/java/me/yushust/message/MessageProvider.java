package me.yushust.message;

import me.yushust.message.config.Specifier;
import me.yushust.message.internal.MessageProviderImpl;
import me.yushust.message.language.Linguist;
import me.yushust.message.source.MessageSource;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.StringList;

public interface MessageProvider {

  Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  String format(Object entity, String text);

  String format(
    TrackingContext context,
    String path
  );

  StringList formatMany(
    TrackingContext context,
    String path
  );

  default String format(
      Object entity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities
  ) {
    return format(
      new TrackingContext(
        entity,
        "",
        jitEntities,
        replacements,
        ReplacePack.EMPTY,
        this
      ),
      path
    );
  }

  default StringList formatMany(
      Object entity,
      String path,
      ReplacePack replacements,
      Object[] jitEntities
  ) {
    return formatMany(
      new TrackingContext(
        entity,
        "",
        jitEntities,
        replacements,
        ReplacePack.EMPTY,
        this
      ),
      path
    );
  }

  default String get(Object entity, String path, Object... jitEntities) {
    return format(entity, path, ReplacePack.EMPTY, jitEntities);
  }

  default String replacing(Object entity, String path, Object... replacements) {
    return format(entity, path, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY);
  }

  default StringList getMany(Object entity, String messagePath, Object... jitEntities) {
    return formatMany(entity, messagePath, ReplacePack.EMPTY, jitEntities);
  }

  default StringList replacingMany(Object entity, String messagePath, Object... replacements) {
    return formatMany(entity, messagePath, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY);
  }

  MessageSource getSource();

  /**
   * Returns the {@link Linguist} for the specified entity,
   * If a {@link Linguist} was not given, returns a dummy {@link Linguist}
   * @see Linguist#dummy()
   */
  <T> Linguist<T> getLanguageProvider(Class<T> entityType);

  static MessageProvider create(
      MessageProvider repository,
      Specifier... specifiers
  ) {
    return new MessageProviderImpl(repository, specifiers);
  }

}
