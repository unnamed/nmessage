package me.yushust.message.send;

import me.yushust.message.util.ReplacePack;

public interface Messenger {

  String DEFAULT_MODE = "default";

  Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  void dispatch(
      Object entityOrEntities, // Can be a single entity or an Iterable<?>
      String path,
      String mode,
      ReplacePack replacements,
      Object[] jitEntities,
      Object[] orderedArgs
  );

  default void send(Object entityOrEntities, String mode, String path, Object... jitEntities) {
    dispatch(entityOrEntities, path, mode, ReplacePack.EMPTY, jitEntities, EMPTY_OBJECT_ARRAY);
  }

  default void send(Object entityOrEntities, String path, Object... jitEntities) {
    send(entityOrEntities, DEFAULT_MODE, path, jitEntities);
  }

  default void sendReplacing(Object entityOrEntities, String mode, String path, Object... replacements) {
    dispatch(entityOrEntities, path, mode, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY, EMPTY_OBJECT_ARRAY);
  }

  default void sendReplacing(Object entityOrEntities, String path, Object... replacements) {
    sendReplacing(entityOrEntities, DEFAULT_MODE, path, replacements);
  }

  default void sendFormatting(Object entityOrEntities, String mode, String path, Object... args) {
    dispatch(entityOrEntities, path, mode, ReplacePack.EMPTY, EMPTY_OBJECT_ARRAY, args);
  }

  default void sendFormatting(Object entityOrEntities, String path, Object... args) {
    sendFormatting(entityOrEntities, DEFAULT_MODE, path, args);
  }

}
