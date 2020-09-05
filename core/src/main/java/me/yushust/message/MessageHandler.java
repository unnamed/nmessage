package me.yushust.message;

import me.yushust.message.localization.LanguageProvider;
import me.yushust.message.provide.ContextualMessageRepository;
import me.yushust.message.provide.ProvideContext;

import java.util.function.UnaryOperator;

/**
 * The library main class
 * @param <T> The type of the property holder
 */
public interface MessageHandler<T> extends MessageProvider<T> {

    /**
     * Gets a message using the language of the
     * property holder (using {@link LanguageProvider}),
     * the message is located using the messagePath.
     * If the message is not found, it returns null
     * or the path as specified with {@link ProvideStrategy}
     * @param messagePath The message location
     * @param context Internal parameter, indicates the
     *                paths that depend of this message
     *                and the ignored formatters.
     * @return The message, null or the path.
     */
    String getMessage(ProvideContext<T> context, String messagePath);

    /**
     * Sends a message to the specified receiver using
     * {@link MessageConsumer}
     * @param propertyHolder The message receiver
     * @param messagePath The message location
     */
    void sendMessage(T propertyHolder, String messagePath);

    /**
     * Sends a message to the specified receivers using
     * {@link MessageConsumer}
     * @param entities The message receivers
     * @param messagePath The message location
     * @param interceptor The just-in-time interceptor, util for
     *                    replacing variables
     */
    void sendMessage(Iterable<? extends T> entities, String messagePath, UnaryOperator<String> interceptor);

    /**
     * Sends a message to the specified receivers using
     * {@link MessageConsumer}
     * @param propertyHolders The message receivers
     * @param messagePath The message location
     */
    default void sendMessage(Iterable<T> propertyHolders, String messagePath) {
        sendMessage(propertyHolders, messagePath, UnaryOperator.identity());
    }

    ContextualMessageRepository<T> withContext(ProvideContext<T> context);

    static <T> MessageProviderBuilder<T> builder() {
        return new MessageProviderBuilder<>();
    }

}
