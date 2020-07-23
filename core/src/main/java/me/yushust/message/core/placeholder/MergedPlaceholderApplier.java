package me.yushust.message.core.placeholder;

import java.util.HashSet;
import java.util.Set;

public class MergedPlaceholderApplier<T> implements PlaceholderApplier<T> {

    private final Set<PlaceholderApplier<T>> delegates = new HashSet<>();

    @Override
    public String applyPlaceholders(T propertyHolder, String text) {
        String finalText = text;
        for (PlaceholderApplier<T> applier : delegates) {
            finalText = applier.applyPlaceholders(propertyHolder, finalText);
        }
        return finalText;
    }

    public void addPlaceholderApplier(PlaceholderApplier<T> placeholderApplier) {
        this.delegates.add(placeholderApplier);
    }

}
