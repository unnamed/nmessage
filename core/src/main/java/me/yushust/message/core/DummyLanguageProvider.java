package me.yushust.message.core;

// A dummy language provider that returns the default language
// for all language holders
public class DummyLanguageProvider<T> implements LanguageProvider<T> {

    // lazy-initialized singleton instance
    private static final LanguageProvider<?> INSTANCE = new DummyLanguageProvider<>();

    @Override
    public String getLanguage(T languageHolder) {
        return "def";
    }

    @SuppressWarnings("unchecked")
    public static <T> LanguageProvider<T> getInstance() {
        return (LanguageProvider<T>) INSTANCE;
    }

}
