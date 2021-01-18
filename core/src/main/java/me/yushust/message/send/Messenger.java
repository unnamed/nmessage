package me.yushust.message.send;

import me.yushust.message.ReplacePack;
import me.yushust.message.mode.Mode;

public interface Messenger {

  Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  Mode defaultMode();

  void dispatch(
      Object entityOrEntities, // Can be a single entity or an Iterable<?>
      String path,
      Mode mode,
      ReplacePack replacements,
      Object[] jitEntities,
      Object[] orderedArgs
  );

  default void send(Object entityOrEntities, Mode mode, String path, Object... jitEntities) {
    dispatch(entityOrEntities, path, mode, ReplacePack.EMPTY, jitEntities, EMPTY_OBJECT_ARRAY);
  }

  default void send(Object entityOrEntities, String path, Object... jitEntities) {
    send(entityOrEntities, defaultMode(), path, jitEntities);
  }

  default void sendReplacing(Object entityOrEntities, Mode mode, String path, Object... replacements) {
    dispatch(entityOrEntities, path, mode, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY, EMPTY_OBJECT_ARRAY);
  }

  default void sendReplacing(Object entityOrEntities, String path, Object... replacements) {
    sendReplacing(entityOrEntities, defaultMode(), path, replacements);
  }

  default void sendFormatting(Object entityOrEntities, Mode mode, String path, Object... args) {
    dispatch(entityOrEntities, path, mode, ReplacePack.EMPTY, EMPTY_OBJECT_ARRAY, args);
  }

  default void sendFormatting(Object entityOrEntities, String path, Object... args) {
    sendFormatting(entityOrEntities, defaultMode(), path, args);
  }

}
