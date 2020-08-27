package me.yushust.message.core;

import me.yushust.message.core.localization.LanguageProvider;

import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * The library main class
 * @param <T> The type of the property holder
 */
public interface MessageProvider<T> extends MessageRepository, MessageFormatter<T> {

    /**
     * Gets a message using the language of the
     * property holder (using {@link LanguageProvider}),
     * the message is located using the messagePath.
     * If the message is not found, it returns null
     * or the path as specified with {@link ProvideStrategy}
     * @param propertyHolder The property holder
     * @param messagePath The message location
     * @param linkedPaths Internal parameter, indicates the
     *                    paths that depend of this message,
     *                    if messagePath is present in
     *                    linkedPaths, the return value is
     *                    determined by {@link MessageRepository#getMessage}
     *                    (Without interceptions)
     * @return The message, null or the path.
     */
    String getMessage(T propertyHolder, String messagePath, Collection<String> linkedPaths);

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
     * @param propertyHolders The message receivers
     * @param messagePath The message location
     * @param interceptor The just-in-time interceptor, util for
     *                    replacing variables
     */
    void sendMessage(Iterable<T> propertyHolders, String messagePath, UnaryOperator<String> interceptor);

    /**
     * Sends a message to the specified receivers using
     * {@link MessageConsumer}
     * @param propertyHolders The message receivers
     * @param messagePath The message location
     */
    default void sendMessage(Iterable<T> propertyHolders, String messagePath) {
        sendMessage(propertyHolders, messagePath, UnaryOperator.identity());
    }

    static <T> MessageProviderBuilder<T> builder() {
        return new MessageProviderBuilder<>();
    }

}
