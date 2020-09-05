package me.yushust.message;

import org.jetbrains.annotations.Nullable;

/**
 * Strategy executed if a message
 * is not present in a file.
 */
@FunctionalInterface
public interface ProvideStrategy {

    /**
     * The path is returned, for example:
     * If I use {@link MessageHandler#getMessage}
     * passing a not existing path,
     * it will return the path.
     */
    ProvideStrategy RETURN_PATH = (propertyHolder, path) -> path;

    /**
     * Null is returned, for example:
     * If I use {@link MessageHandler#getMessage}
     * passing a not existing path, it
     * will return null.
     */
    ProvideStrategy RETURN_NULL = (propertyHolder, path) -> null;

    /**
     * Returns the not found message
     * @param language The language
     * @param path The path
     * @return The not found message (Nullable)
     */
    @Nullable
    String getNotFoundMessage(String language, String path);

}
