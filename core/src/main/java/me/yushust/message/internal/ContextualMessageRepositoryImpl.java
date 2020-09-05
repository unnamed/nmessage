package me.yushust.message.internal;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;
import me.yushust.message.handle.StringList;
import me.yushust.message.provide.ContextualMessageRepository;
import me.yushust.message.provide.ProvideContext;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link MessageRepository} that contains
 * a defined context and uses it for delegate the functionality
 * to the main {@link MessageHandler}.
 * @param <T> The entity type
 */
public class ContextualMessageRepositoryImpl<T> implements ContextualMessageRepository<T> {

    private final MessageHandler<T> delegate;
    private final ProvideContext<T> context;

    public ContextualMessageRepositoryImpl(MessageHandler<T> delegate, ProvideContext<T> context) {
        this.delegate = Validate.notNull(delegate, "delegate");
        this.context = Validate.notNull(context, "context");
    }

    /**
     * Unlike the default implementation of {@link MessageRepository},
     * this implementation, upon receiving a null language, uses the
     * language of the entity in the context.
     * @param language The language
     * @param messagePath The message location
     * @return The message
     */
    @Override
    public String getMessage(@Nullable String language, String messagePath) {

        if (language == null) {
            return delegate.getMessage(context, messagePath);
        } else {
            return delegate.getMessage(language, messagePath);
        }
    }

    /**
     * Unlike the default implementation of {@link MessageRepository},
     * this implementation, upon receiving a null language, uses the
     * language of the entity in the context.
     * @param language The language
     * @param messagePath The message location
     * @return The messages
     */
    @Override
    public StringList getMessages(@Nullable String language, String messagePath) {

        if (language == null) {
            return delegate.getMessages(context.getEntity(), messagePath);
        } else {
            return delegate.getMessages(language, messagePath);
        }
    }

    @Override
    public ProvideContext<T> getContext() {
        return context;
    }

}
