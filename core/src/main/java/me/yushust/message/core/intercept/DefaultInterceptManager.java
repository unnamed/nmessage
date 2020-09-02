package me.yushust.message.core.intercept;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.placeholder.PlaceholderBox;
import me.yushust.message.core.placeholder.PlaceholderProvider;
import me.yushust.message.core.provide.ProvideContext;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * {@inheritDoc}
 */
public class DefaultInterceptManager<T> implements InterceptManager<T> {

    private final Map<String, PlaceholderProvider<T>> replacers = new LinkedHashMap<>();
    protected final List<MessageInterceptor<T>> interceptors = new ArrayList<>();
    private final char identifierSeparator = '_';
    protected PlaceholderBox placeholderBox;
    protected String linkedMessagePrefix;
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

        if (messageProvider != null) {
            replacer.setMessageProvider(messageProvider);
        }

        this.replacers.put(replacer.getIdentifier(), replacer);

        return this;
    }

    @Override
    public String convert(ProvideContext<T> context, String text) {

        checkValidMessageProvider();
        requireNonNull(context);
        requireNonNull(text);

        char[] characters = text.toCharArray();
        StringBuilder builder = new StringBuilder(characters.length);
        StringBuilder identifier = new StringBuilder();
        StringBuilder placeholder = new StringBuilder();

        for (int i = 0; i < characters.length; i++) {

            char character = characters[i];

            if (character != placeholderBox.getStart() || i + 1 >= characters.length) {
                builder.append(character);
                continue;
            }

            boolean closed = false;
            boolean identified = false;

            while (++i < characters.length) {

                char current = characters[i];

                if (current == placeholderBox.getEnd()) {
                    closed = true;
                    break;
                }

                if (!identified && current == identifierSeparator){
                    identified = true;
                    continue;
                }

                if (identified) {
                    placeholder.append(current);
                } else {
                    identifier.append(current);
                }
            }

            String identifierString = identifier.toString().toLowerCase();
            String placeholderString = placeholder.toString();

            identifier.setLength(0);
            placeholder.setLength(0);

            if (!closed) {
                builder
                        .append(placeholderBox.getEnd())
                        .append(identifierString);

                if (identified) {
                    builder
                            .append(identifierSeparator)
                            .append(placeholderString);
                }
                continue;
            }

            Optional<PlaceholderProvider<T>> optionalReplacer = findProvider(identifierString);

            if (optionalReplacer.isPresent()) {

                PlaceholderProvider<T> provider = optionalReplacer.get();

                if (context.ignores(provider)) {
                    appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
                    continue;
                }

                String value = provider.replace(context, placeholderString);

                if (value == null) {
                    appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
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

                String message = context.getProvider().getMessage(
                        context, path
                );

                if (message != null) {
                    builder.append(message);
                    continue;
                }

                context.getLinkedPaths().remove(path);
            }

            appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
        }

        text = builder.toString();

        for (MessageInterceptor<T> interceptor : this.interceptors) {
            if (context.ignores(interceptor)) {
                continue;
            }
            text = interceptor.replace(context, text);
        }

        return text;
    }

    @Override
    public void setPlaceholderBox(PlaceholderBox box) {
        requireNonNull(box);
        this.placeholderBox = box;
    }

    @Override
    public void setLinkedMessagePrefix(String linkedMessagePrefix) {
        requireNonNull(linkedMessagePrefix);
        if (linkedMessagePrefix.isEmpty()) {
            throw new IllegalArgumentException("The linkedMessagePrefix cannot be an empty string!");
        }
        this.linkedMessagePrefix = linkedMessagePrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PlaceholderProvider<T>> findProvider(String identifier) {
        return Optional.ofNullable(replacers.get(identifier.toLowerCase()));
    }

    protected void appendInvalidPlaceholder(StringBuilder builder, boolean identified, String identifier, String placeholder) {
        builder
                .append(placeholderBox.getStart())
                .append(identifier);

        if (identified) {
            builder
                    .append(identifierSeparator)
                    .append(placeholder);
        }

        builder
                .append(placeholderBox.getEnd());
    }

    protected void checkValidMessageProvider() {
        if (messageProvider == null) {
            throw new IllegalStateException("The message provider isn't defined yet!");
        }
    }

}
