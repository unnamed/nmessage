package me.yushust.message.track;

import me.yushust.message.MessageProvider;
import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.format.PlaceholderReplacer;
import me.yushust.message.impl.AbstractMessageProvider;
import me.yushust.message.util.StringList;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link MessageProvider} linked to
 * a {@link TrackingContext}, used to detect cyclic
 * linked messages and ease the recursive use of the
 * {@link MessageProvider} in {@link PlaceholderProvider}
 * instances.
 *
 * <p>This replaces the old cyclic linked messages detection
 * that used a ThreadLocal to retain the contexts by thread</p>
 */
public final class ContextRepository
        extends AbstractMessageProvider
        implements MessageProvider {

    /**
     * The linked tracking context
     */
    private final TrackingContext context;

    /**
     * The delegated message provider
     */
    private final MessageProvider provider;

    /**
     * Constructs a new instance linking the
     * given {@code context} to this contextual
     * message repository and partially delegating
     * the functionality to the given {@code provider}
     */
    public ContextRepository(
            TrackingContext context,
            MessageProvider provider
    ) {
        super(provider.getSource(), provider.getConfig());
        this.context = context;
        this.provider = provider;
    }

    /**
     * Gets the message partially using the linked context,
     * at the specified {@code path}. The context is "partially"
     * used because the language is changed to the specified
     * {@code language}, the entity and the other format properties
     * persists in the new context
     *
     * @see TrackingContext#with
     */
    public String get(String language, String path) {
        return provider.format(context.with(language), path);
    }

    /**
     * Contextually gets the message at the specified
     * {@code path}. The entity and jit-entities from
     * the linked context are used to this.
     *
     * @return The message (all formatted)
     */
    public String get(String path) {
        return provider.format(context, path);
    }

    /**
     * Similar to {@link ContextRepository#get(String)}
     * but it's for {@link StringList} results
     *
     * <p>So it gets the messages partially using the linked
     * context, at the specified {@code path}. The context is
     * "partially" used because the language is changed to the
     * specified {@code language}, the entity and the other
     * format properties persists in the new context</p>
     *
     * @return The messages (all formatted)
     */
    public StringList getMany(@Nullable String language, String messagePath) {
        return provider.formatMany(
                language != null
                        ? context.with(language)
                        : context,
                messagePath
        );
    }

    /**
     * Similar to {@link ContextRepository#get(String)}
     * but it's for {@link StringList} results
     *
     * <p>So it contextually gets the messages at the
     * specified {@code path}. The entity and jit-entities
     * from the linked context are used to this.</p>
     *
     * @return The messages (all formatted)
     */
    public StringList getMany(String path) {
        return provider.formatMany(context, path);
    }

    //#region Pure method functionality delegations
    @Override
    public String format(
            TrackingContext context,
            String path
    ) {
        return provider.format(context, path);
    }

    @Override
    public StringList formatMany(
            TrackingContext context,
            String path
    ) {
        return provider.formatMany(context, path);
    }

    @Override
    public PlaceholderReplacer getReplacer() {
        return provider.getReplacer();
    }

    @Override
    public String format(Object entity, String text) {
        return provider.format(entity, text);
    }
    //#endregion

    /**
     * Returns the entity linked to this contextual repository
     */
    public Object getEntity() {
        return context.getEntity();
    }

    /**
     * Returns the language used to get the messages in this repository
     */
    public String getLanguage() {
        return context.getLanguage();
    }

}
