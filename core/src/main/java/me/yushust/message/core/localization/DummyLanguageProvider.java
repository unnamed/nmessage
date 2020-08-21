package me.yushust.message.core.localization;

public final class DummyLanguageProvider<T> implements LanguageProvider<T> {

    private static final LanguageProvider<?> INSTANCE = new DummyLanguageProvider<>();

    private DummyLanguageProvider() {
    }

    /**
     * {@inheritDoc}
     * @return null
     */
    @Override
    public String getLanguage(T languageHolder) {
        return null;
    }

    /**
     * The lazy-initialized instance of this
     * DummyLanguageProvider casted to "T"
     * @param <T> The type to convert
     * @return The language provider casted to "T"
     */
    @SuppressWarnings("unchecked")
    public static <T> LanguageProvider<T> getInstance() {
        return (LanguageProvider<T>) INSTANCE;
    }

}
