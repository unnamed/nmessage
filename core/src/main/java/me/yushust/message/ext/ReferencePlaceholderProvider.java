package me.yushust.message.ext;

import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.format.ProviderSettings;
import me.yushust.message.track.ContextRepository;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link PlaceholderProvider} used
 * to reference another path by using its placeholders.
 *
 * <p>For example, given a '%path_other_path%', it will
 * check for the message at 'other_path' and return it.</p>
 */
@ProviderSettings(
        requiresEntity = false
)
public final class ReferencePlaceholderProvider<E>
        implements PlaceholderProvider<E> {

    @Override
    public @Nullable String replace(
            ContextRepository handle,
            E entity,
            String path
    ) {
        return handle.get(path);
    }

}
