package me.yushust.message.core.internal;

import me.yushust.message.core.MessageFormatter;
import me.yushust.message.core.MessageRepository;
import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.intercept.DefaultInterceptManager;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.localization.LanguageProvider;

import static java.util.Objects.requireNonNull;

public class SimpleMessageFormatter<T> implements MessageFormatter<T> {

    private final InterceptManager<T> interceptManager = new DefaultInterceptManager<>();
    private final MessageRepository messageRepository;
    private LanguageProvider<T> languageProvider;

    public SimpleMessageFormatter(MessageRepository messageRepository, LanguageProvider<T> languageProvider) {
        this.messageRepository = messageRepository;
        this.languageProvider = languageProvider;
    }

    @Override
    public String getMessage(T propertyHolder, String messagePath) {

        requireNonNull(propertyHolder);
        requireNonNull(messagePath);

        String language = languageProvider.getLanguage(propertyHolder);
        String message = messageRepository.getMessage(language, messagePath);

        if (message == null) {
            return null;
        }

        return interceptManager.convert(propertyHolder, message);
    }

    @Override
    public StringList getMessages(T propertyHolder, String messagePath) {

        requireNonNull(propertyHolder, messagePath);
        requireNonNull(messagePath, messagePath);

        String language = languageProvider.getLanguage(propertyHolder);
        StringList messages = messageRepository.getMessages(language, messagePath);

        messages.replaceAll(
                line -> {
                    if (line == null) {
                        return null;
                    }
                    return interceptManager.convert(propertyHolder, line);
                }
        );

        return messages;
    }

    @Override
    public void useLanguageProvider(LanguageProvider<T> languageProvider) {
        requireNonNull(languageProvider);
        this.languageProvider = languageProvider;
    }

    @Override
    public InterceptManager<T> getInterceptionManager() {
        return interceptManager;
    }
}
