package me.yushust.message.core.intercept;

import me.yushust.message.core.placeholder.PlaceholderApplier;

public interface ReplacingMessageInterceptor<T> extends MessageInterceptor {

    PlaceholderApplier<T> getPlaceholderApplier();

    void addPlaceholderApplier(PlaceholderApplier<T> placeholderApplier);

    ReplacingMessageInterceptor<T> addInterceptor(MessageInterceptor interceptor);

}
