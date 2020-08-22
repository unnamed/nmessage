package me.yushust.message.core.intercept;

import me.yushust.message.core.placeholder.PlaceholderReplacer;

import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * {@inheritDoc}
 */
public class DefaultInterceptManager<T> implements InterceptManager<T> {

    private final List<PlaceholderReplacer<T>> replacers = new LinkedList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public InterceptManager<T> add(MessageInterceptor interceptor) {

        requireNonNull(interceptor);

        // the message interceptor is converted to a
        // placeholder replacer
        this.replacers.add(
                (propertyHolder, text) -> interceptor.intercept(text)
        );
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InterceptManager<T> add(PlaceholderReplacer<T> replacer) {
        requireNonNull(replacer);
        this.replacers.add(replacer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convert(T propertyHolder, String text) {

        requireNonNull(propertyHolder);
        requireNonNull(text);

        String convertedText = text;
        for (PlaceholderReplacer<T> replacer : this.replacers) {
            convertedText = replacer.replace(propertyHolder, convertedText);
        }

        return convertedText;

    }

}
