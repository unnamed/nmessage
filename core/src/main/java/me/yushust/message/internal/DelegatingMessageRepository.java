package me.yushust.message.internal;

import me.yushust.message.MessageRepository;
import me.yushust.message.StringList;
import me.yushust.message.file.NodeFile;
import me.yushust.message.strategy.Strategy;
import org.jetbrains.annotations.Nullable;

public abstract class DelegatingMessageRepository implements MessageRepository {

  private final MessageRepository delegate;

  public DelegatingMessageRepository(MessageRepository delegate) {
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
  public NodeFile in(String lang) {
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
