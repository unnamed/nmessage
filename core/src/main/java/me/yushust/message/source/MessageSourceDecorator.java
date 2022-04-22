package me.yushust.message.source;

import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for creating
 * {@link MessageSource}s using
 * a fluent builder
 */
public final class MessageSourceDecorator {

    // the current source, initially it's the base
    // then it's replaced by its wrappers
    private MessageSource source;

    private MessageSourceDecorator(MessageSource base) {
        this.source = base;
    }

    /**
     * Creates a new {@link MessageSourceDecorator} for the specified {@code base}
     */
    public static MessageSourceDecorator decorate(MessageSource base) {
        Validate.isNotNull(base, "base");
        return new MessageSourceDecorator(base);
    }

    /**
     * Adds a fallback language to the current {@code source}.
     * The new source now uses the specified {@code fallbackLanguage}
     * when the messages aren't present in the required language.
     */
    public MessageSourceDecorator addFallbackLanguage(String fallbackLanguage) {
        Validate.isNotEmpty(fallbackLanguage);
        this.source = new WithFallbackLanguage(source, fallbackLanguage);
        return this;
    }

    public MessageSource get() {
        return source;
    }

    /**
     * It's a {@link MessageSource} decorator that
     * adds a fallback language used when a message
     * isn't present in the required language.
     */
    private static class WithFallbackLanguage
            implements MessageSource {

        // the delegated source
        private final MessageSource delegate;
        // the fallback language
        private final String fallback;

        private WithFallbackLanguage(
                MessageSource delegate,
                String fallback
        ) {
            this.delegate = delegate;
            this.fallback = fallback;
        }

        @Override
        @Nullable
        public Object get(@Nullable String language, String path) {
            if (language == null) {
                return delegate.get(fallback, path);
            }
            Object value = delegate.get(language, path);
            if (value == null && !language.equals(fallback)) {
                value = delegate.get(fallback, path);
            }
            return value;
        }

        @Override
        public char getSectionSeparator() {
            return delegate.getSectionSeparator();
        }

        @Override
        public void load(String language) {
            delegate.load(language);
        }

    }

}
