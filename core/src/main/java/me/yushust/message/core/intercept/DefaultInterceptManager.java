package me.yushust.message.core.intercept;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.placeholder.PlaceholderBox;
import me.yushust.message.core.placeholder.PlaceholderProvider;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * {@inheritDoc}
 */
public class DefaultInterceptManager<T> implements InterceptManager<T> {

    private final Map<String, PlaceholderProvider<T>> replacers = new LinkedHashMap<>();
    protected final List<MessageInterceptor<T>> interceptors = new ArrayList<>();
    protected final PlaceholderBox placeholderBox;
    protected final String linkedMessagePrefix;
    protected MessageProvider<T> messageProvider;

    public DefaultInterceptManager(PlaceholderBox placeholderBox, String linkedMessagePrefix) {
        requireNonNull(placeholderBox);
        requireNonNull(linkedMessagePrefix);
        this.placeholderBox = placeholderBox;
        this.linkedMessagePrefix = linkedMessagePrefix.toLowerCase();
    }

    public DefaultInterceptManager(String linkedMessagePrefix) {
        this(PlaceholderBox.DEFAULT, linkedMessagePrefix);
    }

    public DefaultInterceptManager() {
        this("path_");
    }

    @Override
    public void setMessageProvider(MessageProvider<T> newMessageProvider) {

        if (this.messageProvider != null) {
            throw new IllegalStateException("The message provider is already defined!");
        }

        requireNonNull(newMessageProvider);
        this.messageProvider = newMessageProvider;
    }

    @Override
    public MessageProvider<T> getMessageProvider() {
        checkValidMessageProvider();
        return messageProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InterceptManager<T> add(MessageInterceptor<T> interceptor) {

        checkValidMessageProvider();
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
    public InterceptManager<T> add(PlaceholderProvider<T> replacer) {

        checkValidMessageProvider();
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
    public String convert(InterceptContext<T> context, String text, Collection<String> linkedPaths) {

        checkValidMessageProvider();
        requireNonNull(context);
        requireNonNull(text);

        char[] characters = text.toCharArray();
        StringBuilder builder = new StringBuilder(characters.length);
        StringBuilder placeholder = new StringBuilder();

        for (int i = 0; i < characters.length; i++) {

            char character = characters[i];

            if (character != placeholderBox.getStart() || i + 1 >= characters.length) {
                builder.append(character);
                continue;
            }

            boolean closed = false;

            while (++i < characters.length) {

                char current = characters[i];

                if (current == placeholderBox.getEnd()) {
                    closed = true;
                    break;
                }

                placeholder.append(current);
            }

            String placeholderString = placeholder.toString();
            placeholder.setLength(0);

            if (!closed) {
                builder
                        .append(placeholderBox.getEnd())
                        .append(placeholderString);
                continue;
            }

            Optional<PlaceholderProvider<T>> optionalReplacer = findProvider(placeholderString);

            if (optionalReplacer.isPresent()) {

                String value = optionalReplacer.get()
                        .replace(context, placeholderString.toLowerCase());

                if (value == null) {
                    appendInvalidPlaceholder(builder, placeholderBox, placeholderString);
                    continue;
                }

                // sets the placeholder value
                builder.append(value);
                continue;
            }

            if (placeholderString.toLowerCase().startsWith(linkedMessagePrefix)) {

                String path = placeholderString
                        .substring(linkedMessagePrefix.length())
                        .toLowerCase();

                String message = context.getMessageProvider().getMessage(
                    context.getEntity(), path, linkedPaths
                );

                if (message != null) {
                    builder.append(message);
                    continue;
                }

                linkedPaths.remove(path);
            }

            appendInvalidPlaceholder(builder, placeholderBox, placeholderString);
        }

        return applyInterceptors(context, builder.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PlaceholderProvider<T>> findProvider(String placeholder) {
        return Optional.ofNullable(replacers.get(placeholder.toLowerCase()));
    }

    protected void appendInvalidPlaceholder(StringBuilder builder, PlaceholderBox box, String placeholder) {
        builder
                .append(box.getStart())
                .append(placeholder)
                .append(box.getEnd());
    }

    protected String applyInterceptors(InterceptContext<T> context, String text) {
        for (MessageInterceptor<T> interceptor : this.interceptors) {
            text = interceptor.replace(context, text);
        }
        return text;
    }

    protected void checkValidMessageProvider() {
        if (messageProvider == null) {
            throw new IllegalStateException("The message provider isn't defined yet!");
        }
    }

}
