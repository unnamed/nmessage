package me.yushust.message.impl;

import me.yushust.message.MessageProvider;
import me.yushust.message.config.ConfigurationHandle;
import me.yushust.message.language.Linguist;
import me.yushust.message.resolve.EntityResolver;
import me.yushust.message.source.MessageSource;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.ReplacePack;
import me.yushust.message.util.StringList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public abstract class AbstractMessageProvider
        implements MessageProvider {

    protected static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    protected final MessageSource source;
    protected final ConfigurationHandle config;

    protected AbstractMessageProvider(
            MessageSource source,
            ConfigurationHandle config
    ) {
        this.source = source;
        this.config = config;
    }

    @Override
    public String format(
            Object entity,
            String path,
            ReplacePack replacements,
            Object[] jitEntities
    ) {
        entity = resolve(entity);
        return format(
                new TrackingContext(
                        entity,
                        getLanguage(entity),
                        jitEntities,
                        replacements,
                        Collections.emptyMap(),
                        this
                ),
                path
        );
    }

    @Override
    public StringList formatMany(
            Object entity,
            String path,
            ReplacePack replacements,
            Object[] jitEntities
    ) {
        entity = resolve(entity);
        return formatMany(
                new TrackingContext(
                        entity,
                        getLanguage(entity),
                        jitEntities,
                        replacements,
                        Collections.emptyMap(),
                        this
                ),
                path
        );
    }

    @Override
    @Nullable
    @Contract("null -> null")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object resolve(@Nullable Object entity) {
        if (entity == null) {
            return null;
        } else {
            EntityResolver resolver = config.getResolver(
                    entity.getClass()
            );
            if (resolver == null) {
                return entity;
            } else {
                return resolver.resolve(entity);
            }
        }
    }

    @Override
    @Nullable
    @Contract("null -> null")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getLanguage(@Nullable Object entity) {
        if (entity == null) {
            return null;
        } else {
            Linguist linguist = config.getLinguist(entity.getClass());
            if (linguist == null) {
                return null;
            } else {
                return linguist.getLanguage(entity);
            }
        }
    }

    @Override
    public ConfigurationHandle getConfig() {
        return config;
    }

    @Override
    public MessageSource getSource() {
        return source;
    }

    //#region Convenience methods
    @Override
    public String getMessage(String path) {
        return format(null, path, ReplacePack.EMPTY, EMPTY_OBJECT_ARRAY);
    }

    @Override
    public StringList getMessages(String path) {
        return formatMany(null, path, ReplacePack.EMPTY, EMPTY_OBJECT_ARRAY);
    }

    @Override
    public String get(Object entity, String path, Object... jitEntities) {
        return format(entity, path, ReplacePack.EMPTY, jitEntities);
    }

    @Override
    public String replacing(Object entity, String path, Object... replacements) {
        return format(entity, path, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY);
    }

    @Override
    public StringList getMany(Object entity, String messagePath, Object... jitEntities) {
        return formatMany(entity, messagePath, ReplacePack.EMPTY, jitEntities);
    }

    @Override
    public StringList replacingMany(Object entity, String messagePath, Object... replacements) {
        return formatMany(entity, messagePath, ReplacePack.make(replacements), EMPTY_OBJECT_ARRAY);
    }
    //#endregion

}
