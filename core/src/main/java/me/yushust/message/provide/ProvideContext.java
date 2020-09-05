package me.yushust.message.provide;

import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.util.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class ProvideContext<T> {

    private final Set<PlaceholderProvider<T>> ignoredProviders = new HashSet<>();
    private final Collection<String> linkedPaths = new ArrayList<>();
    private final T entity;

    public ProvideContext(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public Collection<String> getLinkedPaths() {
        return linkedPaths;
    }

    public boolean ignores(PlaceholderProvider<T> provider) {
        Validate.notNull(provider, "provider");
        return ignoredProviders.contains(provider);
    }

    public ProvideContext<T> ignore(PlaceholderProvider<T> provider) {
        Validate.notNull(provider, "provider");
        ignoredProviders.add(provider);
        return this;
    }

    public ProvideContext<T> stopIgnoring(PlaceholderProvider<T> provider) {
        Validate.notNull(provider, "provider");
        ignoredProviders.remove(provider);
        return this;
    }

}
