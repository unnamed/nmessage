package me.yushust.message.core.intercept;

import me.yushust.message.core.placeholder.MergedPlaceholderApplier;
import me.yushust.message.core.placeholder.PlaceholderApplier;

import java.util.HashSet;
import java.util.Set;

public class SimpleMessageInterceptor<T> implements ReplacingMessageInterceptor<T> {

    private final Set<MessageInterceptor> delegates = new HashSet<>();
    private final MergedPlaceholderApplier<T> placeholderApplierPack = new MergedPlaceholderApplier<>();

    @Override
    public String intercept(String text) {
        String finalText = text;
        for (MessageInterceptor interceptor : delegates) {
            finalText = interceptor.intercept(finalText);
        }
        return finalText;
    }

    @Override
    public PlaceholderApplier<T> getPlaceholderApplier() {
        return placeholderApplierPack;
    }

    @Override
    public void addPlaceholderApplier(PlaceholderApplier<T> placeholderApplier) {
        placeholderApplierPack.addPlaceholderApplier(placeholderApplier);
    }

    @Override
    public ReplacingMessageInterceptor<T> addInterceptor(MessageInterceptor interceptor) {
        delegates.add(interceptor);
        return this;
    }

}
