package me.yushust.message.track;

import me.yushust.message.AbstractMessageProvider;
import me.yushust.message.MessageProvider;
import me.yushust.message.util.StringList;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link MessageProvider} linked to
 * a {@link TrackingContext}. This replaces the old
 * cyclic linked messages detection that used a ThreadLocal
 * to retain the contexts by thread in the {@link MessageProvider} instance
 */
public class ContextRepository
  extends AbstractMessageProvider
  implements MessageProvider {

  private final TrackingContext context;
  private final MessageProvider provider;

  public ContextRepository(
      TrackingContext context,
      MessageProvider provider
  ) {
    super(provider.getSource(), provider.getConfig());
    this.context = context;
    this.provider = provider;
  }

  public String get(@Nullable String language, String messagePath) {
    return provider.format(
        language != null ?
            context.with(language)
            : context,
        messagePath
    );
  }

  public String get(String messagePath) {
    return get(null, messagePath);
  }

  public StringList getMany(@Nullable String language, String messagePath) {
    return provider.formatMany(
      language != null
        ? context.with(language)
        : context,
      messagePath
    );
  }

  public StringList getMany(String messagePath) {
    return getMany(null, messagePath);
  }

  @Override
  public String format(
    TrackingContext context,
    String path
  ) {
    return provider.format(context, path);
  }

  @Override
  public StringList formatMany(
    TrackingContext context,
    String path
  ) {
    return provider.formatMany(context, path);
  }

  public Object getEntity() {
    return context.getEntity();
  }

  public String getLanguage() {
    return context.getLanguage();
  }

  @Override
  public String format(Object entity, String text) {
    return provider.format(entity, text);
  }
}
