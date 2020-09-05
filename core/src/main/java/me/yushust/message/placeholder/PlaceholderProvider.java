package me.yushust.message.placeholder;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;
import me.yushust.message.placeholder.annotation.ProviderIdentifier;
import me.yushust.message.provide.ContextualMessageRepository;
import me.yushust.message.provide.ProvideContext;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

/**
 * A class that provides a value for the specified
 * parameters and the provided entity.
 * If {@link MessageHandler#getMessage} is called
 * in {@link PlaceholderProvider#replace}, this
 * {@link PlaceholderProvider} is ignored
 * @param <T> The property holder type
 */
public abstract class PlaceholderProvider<T> {

    private MessageHandler<T> handle;

    /**
     * Sets the used {@link MessageHandler} in this
     * class. If the {@link MessageHandler} is already
     * defined, it results in a {@link IllegalStateException}
     * being thrown.
     * @param handle The message handler
     */
    public final void setHandle(MessageHandler<T> handle) {

        Validate.notNull(handle, "handle");
        Validate.state(this.handle == null, "The provider is already defined!");

        this.handle = handle;
    }

    /**
     * Returns the placeholder provider identifier.
     * In case of returning null, the type annotation
     * {@link ProviderIdentifier} will be used.
     * If it isn't present, a {@link IllegalArgumentException}
     * will be thrown.
     * @return The identifier
     */
    @Nullable
    public String getIdentifier() {
        return null;
    }

    public final String replace(ProvideContext<T> context, String parameters) {

        ContextualMessageRepository<T> contextualRepository = handle.withContext(context);
        contextualRepository.getContext().ignore(this);
        return this.replace(
                handle.withContext(context),
                context.getEntity(),
                parameters
        );
    }

    /**
     * Returns a value corresponding to the specified
     * parameters using the properties of
     * the specified property holder.
     * @param repository The contextual repository
     * @param entity The entity
     * @param parameters The parameters
     * @return The modified text
     */
    @Nullable
    protected abstract String replace(MessageRepository repository, T entity, String parameters);

    @Override
    public String toString() {
        return "PlaceholderProvider {" +
                "handle=" + handle +
                "}";
    }
}
