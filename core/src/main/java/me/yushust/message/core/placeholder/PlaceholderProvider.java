package me.yushust.message.core.placeholder;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.format.Formatter;
import me.yushust.message.core.provide.ProvideContext;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A functional interface that provides a value,
 * depending on the parameters and the entity.
 * If {@link MessageProvider#getMessage} is called
 * in {@link PlaceholderProvider#replace}, this
 * {@link PlaceholderProvider} is ignored
 * @param <T> The property holder type
 */
public abstract class PlaceholderProvider<T> implements Formatter {

    private final String identifier;
    private MessageProvider<T> provider;
    private ProvideContext<T> context;

    public PlaceholderProvider(String identifier) {
        this.identifier = requireNonNull(identifier);
    }

    public final void setMessageProvider(MessageProvider<T> provider) {
        requireNonNull(provider);

        if (this.provider != null) {
            throw new IllegalStateException("The provider is already defined!");
        }

        this.provider = provider;
    }

    ProvideContext<T> context(T entity) {
        if (context != null) {
            return context;
        }
        return new ProvideContext<>(provider, entity);
    }

    MessageProvider<T> provider() {
        if (provider == null) {
            throw new IllegalStateException("The provider isn't defined yet!");
        }
        return provider;
    }

    protected final String getMessage(T entity, String path) {
        return provider().getMessage(
                context(entity).ignore(this),
                path
        );
    }

    protected final String getMessage(String path) {
        return provider().getMessage(path);
    }

    /**
     * Returns the placeholder provider identifier.
     * @return The identifier
     */
    public final String getIdentifier() {
        return identifier;
    }

    public final String replace(ProvideContext<T> context, String parameters) {
        this.context = context;
        try {
            return replace(context.getEntity(), parameters);
        } finally {
            this.context = null;
        }
    }

    /**
     * Returns a value corresponding to the specified
     * parameters using the properties of
     * the specified property holder.
     * @param entity The entity
     * @param parameters The parameters
     * @return The modified text
     */
    @Nullable
    public abstract String replace(T entity, String parameters);

    @Override
    public final boolean equals(Object o) {
        // equals must compare identity
        return super.equals(o);
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final String toString() {
        return super.toString();
    }
}
