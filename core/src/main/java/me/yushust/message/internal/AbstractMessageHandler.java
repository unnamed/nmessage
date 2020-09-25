package me.yushust.message.internal;

import me.yushust.message.MessageRepository;
import me.yushust.message.MessageConsumer;
import me.yushust.message.MessageHandler;
import me.yushust.message.handle.StringList;
import me.yushust.message.intercept.InterceptManager;
import me.yushust.message.localization.LanguageProvider;
import me.yushust.message.provide.ContextualMessageRepository;
import me.yushust.message.provide.ProvideContext;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMessageHandler<T> implements MessageHandler<T> {

  protected final MessageRepository repository;
  private final MessageConsumer<T> messageConsumer;
  protected final LanguageProvider<T> languageProvider;
  protected final InterceptManager<T> interceptManager;

  protected AbstractMessageHandler(MessageRepository repository,
                                   MessageConsumer<T> messageConsumer,
                                   LanguageProvider<T> languageProvider,
                                   InterceptManager<T> interceptManager) {
    this.repository = repository;
    this.messageConsumer = messageConsumer;
    this.languageProvider = languageProvider;
    this.interceptManager = interceptManager;
  }

  @Override
  public void sendMessage(Object resolvableEntity, String messagePath, Object... jitEntities) {
    T entity = asEntity(resolvableEntity);
    messageConsumer.sendMessage(entity, getMessage(entity, messagePath, jitEntities));
  }

  @Override
  public void sendMessages(Object resolvableEntity, String messagePath, Object... jitEntities) {
    T entity = asEntity(resolvableEntity);
    for (String line : getMessages(entity, messagePath, jitEntities)) {
      messageConsumer.sendMessage(entity, line);
    }
  }

  @Override
  public void sendMessage(Iterable<?> resolvableEntities, String messagePath, Object... jitEntities) {
    for (Object resolvableEntity : resolvableEntities) {
      sendMessage(resolvableEntity, messagePath, jitEntities);
    }
  }

  @Override
  public void sendMessages(Iterable<?> resolvableEntities, String messagePath, Object... jitEntities) {
    for (Object resolvableEntity : resolvableEntities) {
      sendMessages(resolvableEntity, messagePath, jitEntities);
    }
  }

  @Override
  public ContextualMessageRepository<T> withContext(ProvideContext<T> context) {
    return new ContextualMessageRepositoryImpl<>(this, context);
  }

  @Override
  public String getMessage(Object entity, String messagePath, Object... jitEntities) {
    return this.getMessage(
        new ProvideContext<>(asEntity(entity), jitEntities),
        messagePath
    );
  }

  @Override
  public LanguageProvider<T> getLanguageProvider() {
    return languageProvider;
  }

  @Override
  public InterceptManager<T> getInterceptionManager() {
    return interceptManager;
  }

  @Override
  public String getMessage(@Nullable String language, String messagePath) {

    String message = repository.getMessage(language, messagePath);

    if (message == null) {
      return null;
    }

    return interceptManager.convert(
        new ProvideContext<>(null),
        message
    );
  }

  @Override
  public StringList getMessages(@Nullable String language, String messagePath) {

    StringList messages = repository.getMessages(language, messagePath);

    messages.getContents().replaceAll(
        line -> {
          if (line == null) {
            return null;
          }
          return interceptManager.convert(new ProvideContext<>(null), line);
        }
    );

    return messages;
  }

}
