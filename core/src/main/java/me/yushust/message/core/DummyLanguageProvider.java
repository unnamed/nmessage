package me.yushust.message.core;

import static java.util.Objects.requireNonNull;

// A dummy language provider that returns the default language
// for all language holders
public class DummyLanguageProvider<T> implements LanguageProvider<T> {

    private final String defaultLanguage;

    public DummyLanguageProvider(String defaultLanguage) {
        this.defaultLanguage = requireNonNull(defaultLanguage);
    }

    @Override
    public String getLanguage(T languageHolder) {
        return this.defaultLanguage;
    }

}
