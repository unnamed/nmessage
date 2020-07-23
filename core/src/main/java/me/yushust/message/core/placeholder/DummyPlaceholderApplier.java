package me.yushust.message.core.placeholder;

// A dummy language provider that returns the same text
// for all property holders
public class DummyPlaceholderApplier<T> implements PlaceholderApplier<T> {

    // lazy-initialized singleton instance
    private static final PlaceholderApplier<?> INSTANCE = new DummyPlaceholderApplier<>();

    @Override
    public String applyPlaceholders(T propertyHolder, String text) {
        return text;
    }

    @SuppressWarnings("unchecked")
    public static <T> PlaceholderApplier<T> getInstance() {
        return (PlaceholderApplier<T>) INSTANCE;
    }

}
