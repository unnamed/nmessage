package me.yushust.message.core.placeholder;

public interface PlaceholderApplier<T> {

    String applyPlaceholders(T propertyHolder, String text);

}
