package me.yushust.message.core;

import me.yushust.message.core.handle.StringList;
import me.yushust.message.core.intercept.InterceptManager;
import me.yushust.message.core.intercept.MessageInterceptor;
import me.yushust.message.core.localization.LanguageProvider;
import me.yushust.message.core.placeholder.PlaceholderReplacer;

/**
 * A part of the principal interface of this
 * library. Handles stored messages, adds
 * format to them and replaces variables.
 * @param <T> The property holder type
 */
public interface MessageRepository<T> {

    /**
     * Gets a message using the language of the
     * property holder (using {@link LanguageProvider}),
     * the message is located using the messagePath.
     * If the message is not found, it returns null
     * or the path as specified with {@link ProvideStrategy}
     * @param propertyHolder The property holder
     * @param messagePath The message location
     * @return The message, null or the path.
     */
    String getMessage(T propertyHolder, String messagePath);

    /**
     * Gets many messages using the language of the
     * property holder (using {@link LanguageProvider}),
     * the messages are located using the messagePath.
     * If the messages aren't found, it returns null (it's
     * not null, it's an empty list) or the path as
     * specified with {@link ProvideStrategy}.
     * The messages are wrapped with {@link StringList}
     * to facilitate their handling.
     * @param propertyHolder The property holder
     * @param messagePath The messages location
     * @return The messages, wrapped with
     */
    StringList getMessages(T propertyHolder, String messagePath);

    /**
     * Sets the language provider
     * @param languageProvider The language provider
     */
    void useLanguageProvider(LanguageProvider<T> languageProvider);

    /**
     * @return Returns the interception manager, using this
     * you can add simple {@link MessageInterceptor} or
     * {@link PlaceholderReplacer}
     */
    InterceptManager<T> getInterceptionManager();

}
