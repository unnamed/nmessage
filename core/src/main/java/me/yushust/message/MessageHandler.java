package me.yushust.message;

import me.yushust.message.send.impl.MessageHandlerImpl;
import me.yushust.message.util.ReplacePack;

public interface MessageHandler extends MessageProvider {

  String DEFAULT_MODE = "default";

  Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  void dispatch(
      Object entityOrEntities, // Can be a single entity or an Iterable<?>
      String path,
      String mode,
      ReplacePack replacements,
      Object[] jitEntities
  );

  default void send(Object entityOrEntities, String mode, String path, Object... jitEntities) {
    dispatch(entityOrEntities, path, mode, ReplacePack.EMPTY, jitEntities);
  }

  default void send(Object entityOrEntities, String path, Object... jitEntities) {
    send(entityOrEntities, DEFAULT_MODE, path, jitEntities);
  }

  default void sendReplacing(Object entityOrEntities, String mode, String path, Object... replacements) {
    dispatch(entityOrEntities, path, mode, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY);
  }

  default void sendReplacing(Object entityOrEntities, String path, Object... replacements) {
    sendReplacing(entityOrEntities, DEFAULT_MODE, path, replacements);
  }

  static MessageHandler of(MessageProvider provider) {
    return MessageHandlerImpl.of(provider);
  }

}
