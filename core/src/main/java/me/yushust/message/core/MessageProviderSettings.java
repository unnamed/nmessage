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

}