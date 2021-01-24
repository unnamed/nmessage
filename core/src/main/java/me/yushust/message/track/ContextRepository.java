package me.yushust.message.track;

import me.yushust.message.MessageProvider;
import me.yushust.message.ReplacePack;
import me.yushust.message.internal.InternalContext;
import me.yushust.message.internal.MessageProviderImpl;
import me.yushust.message.source.MessageSource;
import me.yushust.message.strategy.Strategy;
import me.yushust.message.util.StringList;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link MessageProvider} linked to
 * a {@link InternalContext}. This replaces the old
 * cyclic linked messages detection that used a ThreadLocal
 * to retain the contexts by thread in the {@link MessageProvider} instance
 */
public class ContextRepository implements MessageProvider {

  private final InternalContext context;
  private final MessageProviderImpl handle;

  public ContextRepository(
      InternalContext context,
      MessageProviderImpl handle
  ) {
    this.context = context;
    this.handle = handle;
  }

  @Override
  public String getMessage(@Nullable String language, String messagePath) {
    return handle.format(
        language != null ?
            context.with(language)
            : context,
        messagePath,
        ReplacePack.EMPTY,
        MessageProvider.EMPTY_OBJECT_ARRAY
    );
  }

  private StringList getMessages(InternalContext ctx, String path) {
    return handle.formatMany(ctx, path, ReplacePack.EMPTY, MessageProvider.EMPTY_OBJECT_ARRAY);
  }

  @Override
  public StringList getMessages(@Nullable String language, String messagePath) {
    return getMessages(
        language != null
            ? context.with(language)
            : context,
        messagePath
    );
  }

  @Override
  public MessageSource in(String lang) {
    return handle.in(lang);
  }

  public Object getEntity() {
    return context.getEntity();
  }

  public String getLanguage() {
    return context.getLanguage();
  }

  @Override
  public Strategy getStrategy() {
    return handle.getStrategy();
  }

  @Override
  public String getDefaultLanguage() {
    return handle.getDefaultLanguage();
  }
}
