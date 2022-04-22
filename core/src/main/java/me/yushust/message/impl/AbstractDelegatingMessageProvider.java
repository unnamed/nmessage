package me.yushust.message.impl;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageProvider;
import me.yushust.message.format.PlaceholderReplacer;
import me.yushust.message.track.TrackingContext;
import me.yushust.message.util.StringList;

public abstract class AbstractDelegatingMessageProvider
        extends AbstractMessageProvider
        implements MessageHandler {

    protected final MessageProvider provider;

    protected AbstractDelegatingMessageProvider(MessageProvider provider) {
        super(provider.getSource(), provider.getConfig());
        this.provider = provider;
    }

    //#region Delegation methods
    @Override
    public String format(Object entity, String text) {
        return provider.format(entity, text);
    }

    @Override
    public String format(TrackingContext context, String path) {
        return provider.format(context, path);
    }

    @Override
    public StringList formatMany(TrackingContext context, String path) {
        return provider.formatMany(context, path);
    }

    @Override
    public PlaceholderReplacer getReplacer() {
        return provider.getReplacer();
    }

    //#endregion

}
