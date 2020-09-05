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

import java.util.function.UnaryOperator;

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
    public void sendMessage(T entity, String messagePath) {
        messageConsumer.sendMessage(entity, getMessage(entity, messagePath));
    }

    @Override
    public void sendMessage(Iterable<? extends T> entities, String messagePath, UnaryOperator<String> interceptor) {
        for (T entity : entities) {
            String message = getMessage(entity, messagePath);
            if (message != null) {
                message = interceptor.apply(message);
                messageConsumer.sendMessage(entity, message);
            }
        }
    }

    @Override
    public ContextualMessageRepository<T> withContext(ProvideContext<T> context) {
        return new ContextualMessageRepositoryImpl<>(this, context);
    }

    @Override
    public String getMessage(T entity, String messagePath) {
        return getMessage(
                new ProvideContext<>(entity),
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
        ProvideContext<T> context = new ProvideContext<>(null);

        messages.getContents().replaceAll(
                line -> {
                    if (line == null) {
                        return null;
                    }
                    return interceptManager.convert(context, line);
                }
        );

        return messages;
    }

}
