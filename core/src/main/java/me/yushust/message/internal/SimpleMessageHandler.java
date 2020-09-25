package me.yushust.message.internal;

import me.yushust.message.EntityResolver;
import me.yushust.message.MessageConsumer;
import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;
import me.yushust.message.generic.EntityResolverRegistry;
import me.yushust.message.handle.StringList;
import me.yushust.message.intercept.InterceptManager;
import me.yushust.message.localization.LanguageProvider;

import me.yushust.message.provide.ProvideContext;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

public class SimpleMessageHandler<T> extends AbstractMessageHandler<T> implements MessageHandler<T> {

  private final EntityResolverRegistry<T> resolverRegistry =
      new EntityResolverRegistry<>();

  public SimpleMessageHandler(MessageConsumer<T> messageConsumer, InterceptManager<T> interceptManager,
                              MessageRepository messageRepository, LanguageProvider<T> languageProvider) {
    super(messageRepository, messageConsumer, languageProvider, interceptManager);
    // ends the construction stage
    this.interceptManager.setHandle(this);
  }

  @Override
  public String getMessage(ProvideContext<T> context, String messagePath) {

    Validate.notNull(context, "context");
    Validate.notNull(messagePath, "messagePath");

    String language = languageProvider.getLanguage(context.getEntity());
    String message = repository.getMessage(language, messagePath);

    if (context.getLinkedPaths().contains(messagePath)) {
      return message;
    }

    context.getLinkedPaths().add(messagePath);

    if (message == null) {
      return null;
    }

    return interceptManager.convert(context, message);
  }

  @Nullable
  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public T asEntity(Object resolvableEntity) {
    if (interceptManager.getEntityType().isInstance(resolvableEntity)) {
      return (T) resolvableEntity;
    }
    Class<?> clazz = resolvableEntity.getClass();
    EntityResolver resolver = resolverRegistry.findResolver(clazz);
    return resolver == null ? null : (T) resolver.resolve(resolvableEntity);
  }

  @Override
  public EntityResolverRegistry<T> getEntityResolverRegistry() {
    return resolverRegistry;
  }

  @Override
  public StringList getMessages(Object entityLike, String messagePath, Object... jitEntities) {

    Validate.notNull(messagePath, "messagePath");

    T entity = entityLike == null ? null : asEntity(entityLike);
    String language = entityLike == null ? null : languageProvider.getLanguage(entity);
    StringList messages = repository.getMessages(language, messagePath);

    messages.replaceAll(
        line -> {
          if (line == null) {
            return null;
          }
          return interceptManager.convert(new ProvideContext<>(entity), line);
        }
    );

    return messages;
  }

}
