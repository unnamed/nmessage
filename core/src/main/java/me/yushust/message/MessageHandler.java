package me.yushust.message;

import me.yushust.message.config.ConfigurationModule;
import me.yushust.message.send.impl.MessageHandlerImpl;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.ReplacePack;

public interface MessageHandler extends MessageProvider {

  String DEFAULT_MODE = "default";

  Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  void dispatch(
      Object entityOrEntities, // Can be a single entity or an Iterable<?>
      String path,
      String mode,
      ReplacePack replacements,
      Object... jitEntities
  );

  default void sendIn(Object entityOrEntities, String mode, String path, Object... jitEntities) {
    dispatch(entityOrEntities, path, mode, ReplacePack.EMPTY, jitEntities);
  }

  default void send(Object entityOrEntities, String path, Object... jitEntities) {
    sendIn(entityOrEntities, DEFAULT_MODE, path, jitEntities);
  }

  default void sendReplacingIn(Object entityOrEntities, String mode, String path, Object... replacements) {
    dispatch(entityOrEntities, path, mode, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY);
  }

  default void sendReplacing(Object entityOrEntities, String path, Object... replacements) {
    sendReplacingIn(entityOrEntities, DEFAULT_MODE, path, replacements);
  }

  static MessageHandler of(MessageProvider provider) {
    return MessageHandlerImpl.of(provider);
  }

  static MessageHandler of(MessageSource source, ConfigurationModule... modules) {
    return of(MessageProvider.create(source, modules));
  }

}
