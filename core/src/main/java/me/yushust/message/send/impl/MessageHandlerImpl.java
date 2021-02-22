package me.yushust.message.send.impl;

import me.yushust.message.MessageProvider;
import me.yushust.message.impl.AbstractDelegatingMessageProvider;
import me.yushust.message.send.MessageSender;
import me.yushust.message.MessageHandler;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.ReplacePack;
import me.yushust.message.util.StringList;
import me.yushust.message.util.Validate;

import java.util.Collections;
import java.util.List;

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

        String language = getLanguage(entity);
        Object message = source.get(language, path);

        TrackingContext context = new TrackingContext(
          entity,
          language,
          jitEntities,
          replacements,
          Collections.emptyMap(),
          this
        );

        context.push(path);

        if (message instanceof List) {
          @SuppressWarnings("unchecked")
          StringList messages = new StringList((List<String>) message);
          replacements
            .replace(messages)
            .replaceAll(text -> {
              text = getReplacer().setPlaceholders(context, text);
              return config.intercept(text);
            });
          sender.send(entity, mode, messages);
        } else {
          String messageStr = getReplacer().getValueProvider()
            .convertObjectToString(path, message);
          messageStr = replacements.replace(messageStr);
          messageStr = getReplacer().setPlaceholders(context, messageStr);
          messageStr = config.intercept(messageStr);
          sender.send(entity, mode, messageStr);
        }

        context.pop();
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
