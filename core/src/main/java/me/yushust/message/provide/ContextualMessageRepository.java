package me.yushust.message.provide;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;

/**
 * Represents a {@link MessageRepository} that contains
 * a defined context and uses it for delegate the functionality
 * to the main {@link MessageHandler}.
 * @param <T> The entity type
 */
public interface ContextualMessageRepository<T> extends MessageRepository {

    /**
     * @return The context of this repository.
     */
    ProvideContext<T> getContext();

}
