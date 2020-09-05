package me.yushust.message.internal;

import me.yushust.message.MessageConsumer;
import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;
import me.yushust.message.handle.StringList;
import me.yushust.message.intercept.InterceptManager;
import me.yushust.message.localization.LanguageProvider;

import me.yushust.message.provide.ProvideContext;
import me.yushust.message.util.Validate;

public class SimpleMessageHandler<T> extends AbstractMessageHandler<T> implements MessageHandler<T> {

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

    @Override
    public StringList getMessages(T entity, String messagePath) {

        Validate.notNull(entity, "entity");
        Validate.notNull(messagePath, "messagePath");

        ProvideContext<T> context = new ProvideContext<>(entity);
        String language = languageProvider.getLanguage(entity);
        StringList messages = repository.getMessages(language, messagePath);

        messages.replaceAll(
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
