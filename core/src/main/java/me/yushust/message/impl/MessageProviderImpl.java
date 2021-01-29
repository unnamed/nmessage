package me.yushust.message.impl;

import me.yushust.message.MessageProvider;
import me.yushust.message.config.ConfigurationHandle;
import me.yushust.message.util.ReplacePack;
import me.yushust.message.format.PlaceholderReplacer;
import me.yushust.message.format.PlaceholderValueProviderImpl;
import me.yushust.message.source.MessageSource;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.StringList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class MessageProviderImpl
  extends AbstractMessageProvider
  implements MessageProvider {

  private final PlaceholderReplacer replacer;

  public MessageProviderImpl(
    MessageSource source,
    ConfigurationHandle configHandle
  ) {
    super(source, configHandle.getWiringContainer());
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
    String message = convertObjectToString(source.get(language, path));

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
    StringList messages = convertObjectToStringList(source.get(language, path));

    context.getLiteralReplacements().replace(messages);

    context.push(path);
    messages.replaceAll(line -> {
      line = replacer.setPlaceholders(context, line);
      return config.intercept(line);
    });
    context.pop();
    return messages;
  }

  private StringList convertObjectToStringList(Object object) {
    if (object instanceof List) {
      @SuppressWarnings("unchecked")
      List<String> list = (List<String>) object;
      return new StringList(list);
    } else if (object == null) {
      return null;
    } else {
      return new StringList(
        Arrays.asList(object.toString().split("\n"))
      );
    }
  }

  private String convertObjectToString(Object object) {
    return Objects.toString(object, null);
  }

}
