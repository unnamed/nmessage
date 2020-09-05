package me.yushust.message;

import me.yushust.message.intercept.DefaultInterceptManager;
import me.yushust.message.intercept.InterceptManager;
import me.yushust.message.intercept.MessageInterceptor;
import me.yushust.message.internal.SimpleMessageHandler;
import me.yushust.message.localization.LanguageProvider;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.placeholder.ReferencePlaceholderProvider;
import me.yushust.message.util.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * A MessageProvider Builder, fluent API.
 * Used to create a MessageProvider
 * @param <T> The property holder type
 */
public final class MessageProviderBuilder<T> {

    // optional values
    private final List<PlaceholderProvider<T>> placeholderProviders = new ArrayList<>();
    private final List<MessageInterceptor> messageInterceptors = new ArrayList<>();
    private InterceptManager<T> interceptManager = new DefaultInterceptManager<>();
    private LanguageProvider<T> languageProvider = LanguageProvider.dummy();
    private MessageConsumer<T> messageConsumer = MessageConsumer.dummy();

    // required value
    private MessageRepository messageRepository;

    public MessageProviderBuilder<T> setRepository(MessageRepository messageRepository) {
        this.messageRepository = Validate.notNull(messageRepository);
        return this;
    }

    public MessageProviderBuilder<T> addProvider(PlaceholderProvider<T> provider) {
        this.placeholderProviders.add(Validate.notNull(provider));
        return this;
    }

    public MessageProviderBuilder<T> addInterceptor(MessageInterceptor interceptor) {
        this.messageInterceptors.add(Validate.notNull(interceptor));
        return this;
    }

    public MessageProviderBuilder<T> setLanguageProvider(LanguageProvider<T> languageProvider) {
        this.languageProvider = Validate.notNull(languageProvider);
        return this;
    }

    public MessageProviderBuilder<T> setMessageConsumer(MessageConsumer<T> messageConsumer) {
        this.messageConsumer = Validate.notNull(messageConsumer);
        return this;
    }

    public MessageProviderBuilder<T> setInterceptManager(InterceptManager<T> interceptManager) {
        this.interceptManager = Validate.notNull(interceptManager);
        return this;
    }

    public MessageHandler<T> build() {

        Validate.state(messageRepository != null, "The message repository isn't setted");

        MessageHandler<T> provider = new SimpleMessageHandler<>(
                messageConsumer, interceptManager,
                messageRepository, languageProvider
        );
        InterceptManager<T> interceptManager = provider.getInterceptionManager();

        interceptManager.registerProvider(new ReferencePlaceholderProvider<>());
        for (MessageInterceptor messageInterceptor : messageInterceptors) {
            interceptManager.registerInterceptor(messageInterceptor);
        }

        for (PlaceholderProvider<T> placeholderProvider : placeholderProviders) {
            interceptManager.registerProvider(placeholderProvider);
        }

        return provider;
    }

}
