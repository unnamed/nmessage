package me.yushust.message.intercept;

import me.yushust.message.placeholder.PlaceholderBox;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.provide.ProvideContext;
import me.yushust.message.util.Validate;

import java.util.*;

/**
 * {@inheritDoc}
 */
public class DefaultInterceptManager<T> extends DefaultFormatterRegistry<T> implements InterceptManager<T> {

    private final char identifierSeparator = '_';
    protected PlaceholderBox placeholderBox;

    public DefaultInterceptManager(PlaceholderBox placeholderBox) {
        this.placeholderBox = Validate.notNull(placeholderBox);
    }

    public DefaultInterceptManager() {
        this.placeholderBox = PlaceholderBox.DEFAULT;
    }

    @Override
    public String convert(ProvideContext<T> context, String text) {

        checkValidHandle();
        Validate.notNull(context);
        Validate.notNull(text);

        if (text.isEmpty()) {
            return "";
        }

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

            Optional<PlaceholderProvider<T>> optionalReplacer = context.getEntity() == null
                    ? getOptionalEntityProvider(identifierString)
                    : getProvider(identifierString);

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

            appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
        }

        text = builder.toString();

        for (MessageInterceptor interceptor : getInterceptors()) {
            text = interceptor.intercept(text);
        }

        return text;
    }

    @Override
    public void setPlaceholderBox(PlaceholderBox box) {
        this.placeholderBox = Validate.notNull(box);
    }

    protected void appendInvalidPlaceholder(StringBuilder builder, boolean identified, String identifier, String placeholder) {

        builder.append(placeholderBox.getStart());
        builder.append(identifier);

        if (identified) {
            builder.append(identifierSeparator);
            builder.append(placeholder);
        }

        builder.append(placeholderBox.getEnd());
    }

}
