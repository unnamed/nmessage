package me.yushust.message.send.impl;

import me.yushust.message.MessageProvider;
import me.yushust.message.impl.AbstractDelegatingMessageProvider;
import me.yushust.message.send.MessageSender;
import me.yushust.message.MessageHandler;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.ReplacePack;
import me.yushust.message.util.Validate;

import java.util.Collections;

public class MessageHandlerImpl
  extends AbstractDelegatingMessageProvider
  implements MessageHandler, MessageProvider { // I just like to explicitly add the interfaces

  private MessageHandlerImpl(MessageProvider delegate) {
    super(delegate);
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void dispatch(
    Object entityOrEntities,
    String path,
    String mode,
    ReplacePack replacements,
    Object[] jitEntities
  ) {
    if (entityOrEntities instanceof Iterable) {
      // support for Iterable<Iterable<...>> because yes
      Iterable<?> iterable = (Iterable<?>) entityOrEntities;
      for (Object element : iterable) {
        dispatch(element, path, mode, replacements, jitEntities);
      }
    } else if (entityOrEntities != null) { // not fail-fast
      Object entity = resolve(entityOrEntities);
      if (entity != null) {
        MessageSender sender = config.getSender(entity.getClass());
        Validate.isNotNull(sender, "No sender specified for " +
          "entity type " + entity.getClass() + " or supertypes");

        String message = format(
          new TrackingContext(
            entity,
            getLanguage(entity),
            jitEntities,
            replacements,
            Collections.emptyMap(),
            this
          ),
          path
        );
        if (message != null) {
          sender.send(entity, mode, message);
        }
      }
    }
  }

  public static MessageHandler of(MessageProvider provider) {
    Validate.isNotNull(provider, "provider");
    if (provider instanceof MessageHandler) {
      return (MessageHandler) provider;
    } else {
      return new MessageHandlerImpl(provider);
    }
  }

}
