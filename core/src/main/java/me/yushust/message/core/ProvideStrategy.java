package me.yushust.message.core;

/**
 * Strategy executed if a message
 * is not present in a file.
 */
public enum ProvideStrategy {

    /**
     * The path is returned, for example:
     * If I use {@link MessageProvider#getMessage}
     * passing a not existing path,
     * it will return the path.
     */
    RETURN_PATH,

    /**
     * Null is returned, for example:
     * If I use {@link MessageProvider#getMessage}
     * passing a not existing path, it
     * will return null.
     */
    RETURN_NULL

}
