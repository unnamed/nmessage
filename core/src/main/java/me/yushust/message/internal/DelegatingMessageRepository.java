package me.yushust.message.internal;

import me.yushust.message.source.MessageSource;
import me.yushust.message.strategy.Strategy;
import me.yushust.message.util.StringList;
import org.jetbrains.annotations.Nullable;

public abstract class DelegatingMessageRepository implements MessageProvider {

  private final MessageProvider delegate;

  public DelegatingMessageRepository(MessageProvider delegate) {
    this.delegate = delegate;
  }

  @Override
  public String getMessage(@Nullable String language, String messagePath) {
    return delegate.getMessage(language, messagePath);
  }

  @Override
  public StringList getMessages(@Nullable String language, String messagePath) {
    return delegate.getMessages(language, messagePath);
  }

  @Override
  public MessageSource in(String lang) {
    return delegate.in(lang);
  }

  @Override
  public Strategy getStrategy() {
    return delegate.getStrategy();
  }

  @Override
  public String getDefaultLanguage() {
    return delegate.getDefaultLanguage();
  }
}
