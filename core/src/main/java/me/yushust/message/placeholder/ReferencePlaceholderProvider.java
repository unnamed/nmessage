package me.yushust.message.placeholder;

import me.yushust.message.MessageRepository;
import me.yushust.message.placeholder.annotation.OptionalEntity;
import me.yushust.message.placeholder.annotation.ProviderIdentifier;
import me.yushust.message.provide.ContextualMessageRepository;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

@OptionalEntity
@ProviderIdentifier("path")
public class ReferencePlaceholderProvider<T> extends PlaceholderProvider<T> {

    private final String prefix;

    public ReferencePlaceholderProvider(String prefix) {
        this.prefix = Validate.notEmpty(prefix);
    }

    public ReferencePlaceholderProvider() {
        this.prefix = "path";
    }

    @Override
    @Nullable
    public String getIdentifier() {
        return prefix;
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public String replace(MessageRepository repository, T entity, String parameters) {

        Validate.argument(repository instanceof ContextualMessageRepository,
                "The provided repository isn't an instance of ContextualMessageRepository!");

        ((ContextualMessageRepository<T>) repository).getContext()
            .stopIgnoring(this);

        return repository.getMessage(parameters);
    }

}
