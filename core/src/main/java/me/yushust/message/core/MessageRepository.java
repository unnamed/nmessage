package me.yushust.message.core;

import me.yushust.message.core.handle.StringList;
import org.jetbrains.annotations.Nullable;

/**
 * A part of the principal interface of this
 * library. Handles stored messages, adds
 * format to them and replaces variables.
 */
public interface MessageRepository {

    /**
     * Finds the message using the language and the messagePath
     * If the message is not found, it returns null
     * or the path as specified with {@link ProvideStrategy}
     * @param language The language
     * @param messagePath The message location
     * @return The message, null or the path.
     */
    String getMessage(@Nullable String language, String messagePath);

    /**
     * Finds the messages using the language and the messagePath
     * If the messages are not found, it returns a singleton
     * list containing null or the path as specified
     * with {@link ProvideStrategy}
     * @param language The language
     * @param messagePath The message location
     * @return The messages, wrapped with StringList
     */
    StringList getMessages(@Nullable String language, String messagePath);

}
