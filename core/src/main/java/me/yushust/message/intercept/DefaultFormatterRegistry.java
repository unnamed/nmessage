package me.yushust.message.intercept;

import me.yushust.message.MessageHandler;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.util.Providers;
import me.yushust.message.util.Validate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFormatterRegistry<T> implements FormatterRegistry<T> {

    private final Map<String, PlaceholderProvider<T>> providers = new ConcurrentHashMap<>();
    private final Map<String, PlaceholderProvider<T>> optionalEntityProviders = new ConcurrentHashMap<>();
    private final List<MessageInterceptor> interceptors = new LinkedList<>();

    private MessageHandler<T> handle;

    @Override
    public void setHandle(MessageHandler<T> handle) {
        Validate.notNull(handle, "handle");
        Validate.state(this.handle == null, "The message handler is already defined!");
        this.handle = handle;
    }

    @Override
    public FormatterRegistry<T> registerInterceptor(MessageInterceptor interceptor) {

        Validate.notNull(interceptor, "interceptor");
        checkValidHandle();

        interceptors.add(interceptor);
        return this;
    }

    @Override
    public FormatterRegistry<T> registerProvider(PlaceholderProvider<T> provider) {

        Validate.notNull(provider, "provider");
        checkValidHandle();

        provider.setHandle(handle);
        String identifier = Providers.getIdentifier(provider);
        providers.put(identifier, provider);

        if (!Providers.requiresEntity(provider)) {
            optionalEntityProviders.put(identifier, provider);
        }
        return this;
    }

    @Override
    public Optional<PlaceholderProvider<T>> getProvider(String identifier) {
        Validate.notEmpty(identifier);
        return Optional.ofNullable(
                providers.get(identifier.toLowerCase())
        );
    }

    @Override
    public Optional<PlaceholderProvider<T>> getOptionalEntityProvider(String identifier) {
        Validate.notEmpty(identifier);
        return Optional.ofNullable(
                optionalEntityProviders.get(identifier.toLowerCase())
        );
    }

    @Override
    public List<MessageInterceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptors);
    }

    protected void checkValidHandle() {
        Validate.state(this.handle != null, "The message handler isn't defined yet!");
    }

}
