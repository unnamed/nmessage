package me.yushust.message.core;

import me.yushust.message.core.intercept.DefaultInterceptManager;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.intercept.MessageInterceptor;
import me.yushust.message.core.internal.SimpleMessageProvider;
import me.yushust.message.core.localization.LanguageProvider;
import me.yushust.message.core.placeholder.PlaceholderProvider;
import me.yushust.message.core.placeholder.ReferencePlaceholderProvider;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A MessageProvider Builder, fluent API.
 * Used to create a MessageProvider
 * @param <T> The property holder type
 */
public final class MessageProviderBuilder<T> {

    // optional values
    private final List<PlaceholderProvider<T>> placeholderProviders = new ArrayList<>();
    private final List<MessageInterceptor<T>> messageInterceptors = new ArrayList<>();
    private InterceptManager<T> interceptManager = new DefaultInterceptManager<>();
    private LanguageProvider<T> languageProvider = LanguageProvider.dummy();
    private MessageConsumer<T> messageConsumer = MessageConsumer.dummy();

    // required value
    private MessageRepository messageRepository;

    public MessageProviderBuilder<T> setRepository(MessageRepository messageRepository) {
        requireNonNull(messageRepository);
        this.messageRepository = messageRepository;
        return this;
    }

    public MessageProviderBuilder<T> addProvider(PlaceholderProvider<T> replacer) {
        requireNonNull(replacer);
        this.placeholderProviders.add(replacer);
        return this;
    }

    public MessageProviderBuilder<T> addInterceptor(MessageInterceptor<T> messageInterceptor) {
        requireNonNull(messageInterceptor);
        this.messageInterceptors.add(messageInterceptor);
        return this;
    }

    public MessageProviderBuilder<T> setLanguageProvider(LanguageProvider<T> languageProvider) {
        requireNonNull(languageProvider);
        this.languageProvider = languageProvider;
        return this;
    }

    public MessageProviderBuilder<T> setMessageConsumer(MessageConsumer<T> messageConsumer) {
        requireNonNull(messageConsumer);
        this.messageConsumer = messageConsumer;
        return this;
    }

    public MessageProviderBuilder<T> setInterceptManager(InterceptManager<T> interceptManager) {
        requireNonNull(interceptManager);
        this.interceptManager = interceptManager;
        return this;
    }

    public MessageProvider<T> build() {

        if (messageRepository == null) {
            throw new IllegalStateException("The message repository isn't setted!");
        }

        MessageProvider<T> provider = new SimpleMessageProvider<>(
                messageConsumer, interceptManager,
                messageRepository, languageProvider
        );
        InterceptManager<T> interceptManager = provider.getInterceptionManager();

        interceptManager.add(new ReferencePlaceholderProvider<>());
        for (MessageInterceptor<T> messageInterceptor : messageInterceptors) {
            interceptManager.add(messageInterceptor);
        }

        for (PlaceholderProvider<T> placeholderProvider : placeholderProviders) {
            interceptManager.add(placeholderProvider);
        }

        return provider;
    }

}
