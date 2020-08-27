package me.yushust.message.core.intercept;

import me.yushust.message.core.placeholder.PlaceholderBox;
import me.yushust.message.core.placeholder.PlaceholderReplacer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * {@inheritDoc}
 */
public class DefaultInterceptManager<T> implements InterceptManager<T> {

    private final Map<String, PlaceholderReplacer<T>> replacers = new LinkedHashMap<>();
    private final List<MessageInterceptor<T>> interceptors = new ArrayList<>();
    private final String linkedMessagePrefix;

    public DefaultInterceptManager(String linkedMessagePrefix) {
        this.linkedMessagePrefix = linkedMessagePrefix.toLowerCase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InterceptManager<T> add(MessageInterceptor<T> interceptor) {

        requireNonNull(interceptor);

        // the message interceptor is converted to a
        // placeholder replacer
        this.interceptors.add(interceptor);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InterceptManager<T> addReplacer(PlaceholderReplacer<T> replacer) {

        requireNonNull(replacer);

        for (String placeholder : replacer.getPlaceholders()) {
            this.replacers.put(placeholder, replacer);
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convert(InterceptContext<T> context, String text) {

        requireNonNull(context);
        requireNonNull(text);

        PlaceholderBox box = context.getMessageProvider().getPlaceholderBox();

        String convertedText = text;
        char[] characters = convertedText.toCharArray();
        StringBuilder builder = new StringBuilder(characters.length);
        StringBuilder placeholder = new StringBuilder();

        for (int i = 0; i < characters.length; i++) {

            char character = characters[i];

            if (character != box.getStart() || i + 1 >= characters.length) {
                builder.append(character);
                continue;
            }

            boolean closed = false;

            while (++i < characters.length) {

                char current = characters[i];

                if (current == box.getEnd()) {
                    closed = true;
                    break;
                }

                placeholder.append(character);
            }

            String placeholderString = placeholder.toString();
            placeholder.setLength(0);

            if (!closed) {
                // move the placeholder to the builder,
                // because the "placeholder" isn't valid
                builder.append(placeholderString);
                continue;
            }

            Optional<PlaceholderReplacer<T>> optionalReplacer = findReplacer(placeholderString);

            if (!optionalReplacer.isPresent()) {

                if (placeholderString.toLowerCase().startsWith(linkedMessagePrefix)) {

                    String path = placeholderString
                            .substring(linkedMessagePrefix.length())
                            .toLowerCase();

                    // TODO: Pass a collection to getMessage() to avoid StackOverflowError in case of a cyclic linked-messages dependency
                    String message = context.getMessageProvider().getMessage(
                        context.getEntity(), path
                    );
                
                    if (message != null) {
                        builder.append(message);
                        continue;
                    }
                }

                // move the placeholder to the builder
                // because the "placeholder" doesn't exist
                builder.append(placeholderString);
                continue;
            }

            String value = optionalReplacer.get()
                    .replace(context, placeholderString.toLowerCase());

            if (value == null) {
                // move the placeholder to the builder
                // because the placeholder provider is
                // returning an invalid value
                builder.append(placeholderString);
                continue;
            }

            // sets the placeholder value
            builder.append(value);
        }

        convertedText = builder.toString();

        for (MessageInterceptor<T> interceptor : this.interceptors) {
            convertedText = interceptor.replace(context, convertedText);
        }

        return convertedText;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PlaceholderReplacer<T>> findReplacer(String placeholder) {
        return Optional.ofNullable(replacers.get(placeholder.toLowerCase()));
    }

}
