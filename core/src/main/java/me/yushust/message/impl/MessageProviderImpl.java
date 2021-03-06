package me.yushust.message.impl;

import me.yushust.message.MessageProvider;
import me.yushust.message.config.ConfigurationHandle;
import me.yushust.message.util.ReplacePack;
import me.yushust.message.format.PlaceholderReplacer;
import me.yushust.message.format.PlaceholderValueProviderImpl;
import me.yushust.message.source.MessageSource;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.StringList;

import java.util.Collections;

public final class MessageProviderImpl
  extends AbstractMessageProvider
  implements MessageProvider {

  private final PlaceholderReplacer replacer;

  public MessageProviderImpl(
    MessageSource source,
    ConfigurationHandle configHandle
  ) {
    super(source, configHandle);
    this.replacer = new PlaceholderReplacer(
      new PlaceholderValueProviderImpl(config),
      configHandle.getStartDelimiter(),
      configHandle.getEndDelimiter()
    );
  }

  @Override
  public String format(Object entity, String text) {
    text = replacer.setPlaceholders(
      new TrackingContext(
        entity,
        getLanguage(entity),
        EMPTY_OBJECT_ARRAY,
        ReplacePack.EMPTY,
        Collections.emptyMap(),
        this
      ),
      text
    );
    return config.intercept(text);
  }

  @Override
  public String format(TrackingContext context, String path) {
    String language = context.getLanguage();
    String message = replacer.getValueProvider()
      .convertObjectToString(path, source.get(language, path));

    if (message == null) {
      return null;
    }

    message = context.getLiteralReplacements().replace(message);

    context.push(path);
    message = replacer.setPlaceholders(context, message);
    message = config.intercept(message);

    context.pop();
    return message;
  }

  @Override
  public StringList formatMany(TrackingContext context, String path) {
    String language = context.getLanguage();
    StringList messages = replacer.getValueProvider()
      .convertObjectToStringList(path, source.get(language, path));

    context.getLiteralReplacements().replace(messages);

    context.push(path);
    messages.replaceAll(line -> {
      line = replacer.setPlaceholders(context, line);
      return config.intercept(line);
    });
    context.pop();
    return messages;
  }

  @Override
  public PlaceholderReplacer getReplacer() {
    return replacer;
  }

}
