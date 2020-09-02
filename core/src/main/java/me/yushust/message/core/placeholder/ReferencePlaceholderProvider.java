package me.yushust.message.core.placeholder;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.provide.ProvideContext;
import org.jetbrains.annotations.Nullable;

public class ReferencePlaceholderProvider<T> extends PlaceholderProvider<T> {

    public ReferencePlaceholderProvider(String prefix) {
        super(prefix);
    }

    public ReferencePlaceholderProvider() {
        this("path");
    }

    @Override
    @Nullable
    public String replace(T entity, String parameters) {

        MessageProvider<T> provider = provider();
        ProvideContext<T> context = context(entity);

        return provider.getMessage(context, parameters);
    }

}
