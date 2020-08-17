package me.yushust.message.core;

/**
 * Settings for {@link MessageProvider}
 */
public final class MessageProviderSettings {

    private String defaultLanguage = "def";
    private ProvideStrategy provideStrategy = ProvideStrategy.RETURN_PATH;

    public MessageProviderSettings setProvideStrategy(ProvideStrategy provideStrategy) {
        this.provideStrategy = provideStrategy;
        return this;
    }

    public ProvideStrategy getProvideStrategy() {
        return this.provideStrategy;
    }

    public MessageProviderSettings setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
        return this;
    }

    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }

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

}