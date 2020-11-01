package me.yushust.message;

public interface MessageDispatcher {

  Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  void dispatch(Object entity, String path, ReplacePack replacements, Object[] jitEntities, Object[] orderedArgs);

  void dispatch(Iterable<?> entities, String path, ReplacePack replacements, Object[] jitEntities, Object[] orderedArgs);

  default void send(Object entity, String path, Object... jitEntities) {
    dispatch(entity, path, ReplacePack.EMPTY, jitEntities, EMPTY_OBJECT_ARRAY);
  }

  default void send(Iterable<?> entities, String path, Object... jitEntities) {
    dispatch(entities, path, ReplacePack.EMPTY, jitEntities, EMPTY_OBJECT_ARRAY);
  }

  default void sendReplacing(Object entity, String path, Object... replacements) {
    dispatch(entity, path, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY, EMPTY_OBJECT_ARRAY);
  }

  default void sendReplacing(Iterable<?> entities, String path, Object... replacements) {
    dispatch(entities, path, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY, EMPTY_OBJECT_ARRAY);
  }

  default void sendFormatting(Object entity, String path, Object... args) {
    dispatch(entity, path, ReplacePack.EMPTY, EMPTY_OBJECT_ARRAY, args);
  }

  default void sendFormatting(Iterable<?> entities, String path, Object... args) {
    dispatch(entities, path, ReplacePack.EMPTY, EMPTY_OBJECT_ARRAY, args);
  }

}
