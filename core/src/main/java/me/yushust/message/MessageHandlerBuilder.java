package me.yushust.message;

import me.yushust.message.intercept.DefaultInterceptManager;
import me.yushust.message.intercept.InterceptManager;
import me.yushust.message.intercept.MessageInterceptor;
import me.yushust.message.internal.SimpleMessageHandler;
import me.yushust.message.localization.LanguageProvider;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.placeholder.ReferencePlaceholderProvider;
import me.yushust.message.util.Validate;

/**
 * A MessageProvider Builder, fluent API.
 * Used to create a MessageProvider
 * @param <T> The property holder type
 */
public final class MessageHandlerBuilder<T> {

    // optional values
    private InterceptManager<T> interceptManager;
    private LanguageProvider<T> languageProvider = LanguageProvider.dummy();
    private MessageConsumer<T> messageConsumer = MessageConsumer.dummy();

    // required value
    private MessageRepository messageRepository;

    MessageHandlerBuilder(Class<T> entityType) {
        this.interceptManager = new DefaultInterceptManager<>(entityType);
    }

    public MessageHandlerBuilder<T> setRepository(MessageRepository messageRepository) {
        this.messageRepository = Validate.notNull(messageRepository);
        return this;
    }

    public MessageHandlerBuilder<T> addProvider(PlaceholderProvider<T> provider) {
        Validate.notNull(provider, "provider");
        interceptManager.registerProvider(provider);
        return this;
    }

    public <O> MessageHandlerBuilder<T> addExternalProvider(Class<O> entityType, PlaceholderProvider<O> provider) {
        Validate.notNull(entityType, "entityType");
        Validate.notNull(provider, "provider");
        interceptManager.registerProvider(entityType, provider);
        return this;
    }

    public MessageHandlerBuilder<T> addInterceptor(MessageInterceptor interceptor) {
        Validate.notNull(interceptor, "interceptor");
        interceptManager.registerInterceptor(interceptor);
        return this;
    }

    public MessageHandlerBuilder<T> setLanguageProvider(LanguageProvider<T> languageProvider) {
        this.languageProvider = Validate.notNull(languageProvider);
        return this;
    }

    public MessageHandlerBuilder<T> setMessageConsumer(MessageConsumer<T> messageConsumer) {
        this.messageConsumer = Validate.notNull(messageConsumer);
        return this;
    }

    public MessageHandlerBuilder<T> setInterceptManager(InterceptManager<T> interceptManager) {
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

        return provider;
    }

}
