package me.yushust.message;

import me.yushust.message.internal.InternalContext;
import me.yushust.message.internal.MessageHandlerImpl;
import me.yushust.message.strategy.Strategy;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link MessageRepository} linked to
 * a {@link InternalContext}. This replaces the old
 * cyclic linked messages detection that used a ThreadLocal
 * to retain the contexts by thread in the {@link MessageHandler} instance
 */
public class ContextRepository<E> implements MessageRepository {

  private final InternalContext<E> context;
  private final MessageHandlerImpl<E> handle;

  public ContextRepository(
      InternalContext<E> context,
      MessageHandlerImpl<E> handle
  ) {
    this.context = context;
    this.handle = handle;
  }

  private String getMessage(InternalContext<E> ctx, String path) {
    return handle.format(ctx, path, ReplacePack.EMPTY, MessageHandler.EMPTY_OBJECT_ARRAY);
  }

  @Override
  public String getMessage(@Nullable String language, String messagePath) {
    return handle.format(
        language != null ?
            context.with(language)
            : context,
        messagePath,
        ReplacePack.EMPTY,
        MessageHandler.EMPTY_OBJECT_ARRAY
    );
  }

  private StringList getMessages(InternalContext<E> ctx, String path) {
    return handle.formatMany(ctx, path, ReplacePack.EMPTY, MessageHandler.EMPTY_OBJECT_ARRAY);
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

  public E getEntity() {
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
