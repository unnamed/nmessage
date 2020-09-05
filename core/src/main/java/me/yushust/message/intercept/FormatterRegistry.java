package me.yushust.message.intercept;

import me.yushust.message.MessageHandler;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.placeholder.annotation.OptionalEntity;

import java.util.List;
import java.util.Optional;

/**
 * Represents a registry of formatters like
 * {@link PlaceholderProvider} and {@link MessageInterceptor}
 */
public interface FormatterRegistry<T> {

    /**
     * Sets the message handler, if the message handler
     * was already provided, throws a {@link IllegalStateException}
     * This method marks the end of the stage of
     * {@link MessageHandler}'s construction, and the
     * start of a functional {@link MessageHandler}
     * @param handle The new message handler
     */
    void setHandle(MessageHandler<T> handle);

    /**
     * Registers a message interceptor in
     * this {@link FormatterRegistry}. If the
     * interceptor is null, it throws a
     * {@link NullPointerException}
     * @param interceptor The interceptor
     * @return The formatter registry, for a fluent api
     */
    FormatterRegistry<T> registerInterceptor(MessageInterceptor interceptor);

    /**
     * Registers a placeholder provider in this
     * {@link FormatterRegistry}. If the provider
     * is null, it throws a {@link NullPointerException}
     * @param provider The provider
     * @return The formatter registry, for a fluent api
     */
    FormatterRegistry<T> registerProvider(PlaceholderProvider<T> provider);

    /**
     * Finds a provider using its identifier
     * @param identifier The provider identifier
     * @return The placeholder provider
     */
    Optional<PlaceholderProvider<T>> getProvider(String identifier);

    /**
     * Finds a provider annotated with {@link OptionalEntity}
     * and using the provided identifier
     * @param identifier The identifier
     * @return The placeholder provider
     */
    Optional<PlaceholderProvider<T>> getOptionalEntityProvider(String identifier);

    /**
     * @return An unmodifiable list containing the
     * registered interceptors.
     */
    List<MessageInterceptor> getInterceptors();

}
