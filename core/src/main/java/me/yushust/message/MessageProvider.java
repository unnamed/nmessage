package me.yushust.message;

import me.yushust.message.handle.StringList;
import me.yushust.message.intercept.InterceptManager;
import me.yushust.message.intercept.MessageInterceptor;
import me.yushust.message.localization.LanguageProvider;
import me.yushust.message.placeholder.PlaceholderProvider;

/**
 * {@link MessageRepository}'s wrapper, gets the language of
 * property holder using {@link LanguageProvider} and replaces
 * some variables using {@link PlaceholderProvider}s and
 * {@link MessageInterceptor}s
 * @param <T> The entity type
 */
public interface MessageProvider<T> extends MessageRepository {

    /**
     * Gets a message using the language of the
     * entity (using {@link LanguageProvider}),
     * the message is located using the messagePath.
     * If the message is not found, it returns null
     * or the path as specified with {@link ProvideStrategy}
     * @param entity The entity
     * @param messagePath The message location
     * @return The message, null or the path.
     */
    String getMessage(T entity, String messagePath);

    /**
     * Gets many messages using the language of the
     * provided entity (using {@link LanguageProvider}),
     * the messages are located using the messagePath.
     * If the messages aren't found, it returns a
     * singleton list containing null or the path as
     * specified with {@link ProvideStrategy}.
     * The messages are wrapped with {@link StringList}
     * to facilitate their handling.
     * @param entity The entity
     * @param messagePath The messages location
     * @return The messages, wrapped with StringList
     */
    StringList getMessages(T entity, String messagePath);

    /**
     * @return The language provider that is used to
     * get the entities language.
     */
    LanguageProvider<T> getLanguageProvider();

    /**
     * @return Returns the interception manager, using this
     * you can add simple {@link MessageInterceptor} or
     * {@link PlaceholderProvider}
     */
    InterceptManager<T> getInterceptionManager();

}
