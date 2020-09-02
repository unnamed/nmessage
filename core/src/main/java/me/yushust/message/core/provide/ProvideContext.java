package me.yushust.message.core.provide;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.format.Formatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public final class ProvideContext<T> extends InterceptContext<T> {

    private final Set<Formatter> ignoredFormatters = new HashSet<>();
    private final Collection<String> linkedPaths = new ArrayList<>();

    public ProvideContext(MessageProvider<T> provider, T entity) {
        super(provider, entity);
    }

    public Collection<String> getLinkedPaths() {
        return linkedPaths;
    }

    public boolean ignores(Formatter formatter) {
        requireNonNull(formatter);
        return ignoredFormatters.contains(formatter);
    }

    public ProvideContext<T> ignore(Formatter formatter) {
        requireNonNull(formatter);
        this.ignoredFormatters.add(formatter);
        return this;
    }

}
