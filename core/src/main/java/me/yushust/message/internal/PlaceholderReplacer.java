package me.yushust.message.internal;

import me.yushust.message.MessageInterceptor;
import me.yushust.message.strategy.Notify;
import me.yushust.message.strategy.Strategy;
import me.yushust.message.util.Validate;

final class PlaceholderReplacer {

  private static final char IDENTIFIER_SEPARATOR = '_';

  private final char startDelimiter;
  private final char endDelimiter;
  private final MessageHandlerImpl<?> handle;
  private final FormatterRegistry<?> registry;
  private final Strategy strategy;

  PlaceholderReplacer(MessageHandlerImpl<?> handle, char startDelimiter, char endDelimiter) {
    this.handle = handle;
    this.startDelimiter = startDelimiter;
    this.endDelimiter = endDelimiter;
    this.registry = handle.getFormatterRegistry();
    this.strategy = handle.getStrategy();
  }

  String replace(Object entity, String text, Object... jitEntities) {

    Validate.notNull(text);

    // The minimum length to replace placeholders is 3,
    // because "%a_b%" is a valid placeholder
    if (text.length() < 6) {
      return intercept(text);
    }

    // I don't use text.toCharArray() because it copies
    // the entire internal array
    StringBuilder builder = new StringBuilder(text.length());
    StringBuilder identifier = new StringBuilder();
    StringBuilder placeholder = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {

      char character = text.charAt(i);

      if (character != startDelimiter || i + 1 >= text.length()) {
        builder.append(character);
        continue;
      }

      boolean closed = false;
      boolean identified = false;

      while (++i < text.length()) {

        char current = text.charAt(i);

        if (current == endDelimiter) {
          closed = true;
          break;
        } else if (!identified && current == IDENTIFIER_SEPARATOR) {
          identified = true;
        } else if (identified) {
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
            .append(startDelimiter)
            .append(identifierString);

        if (identified) {
          builder
              .append(IDENTIFIER_SEPARATOR)
              .append(placeholderString);
        }
        continue;
      }

      TypeSpecificPlaceholderProvider<?> provider = registry.getProvider(identifierString);

      if (provider == null) {
        appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
        if (identified) {
          strategy.onFail(Notify.Failure.PROVIDER_NOT_FOUND, text);
        }
        continue;
      }

      if (!provider.isCompatible(entity)) {
        boolean found = false;
        if (jitEntities != null) {
          for (Object jitEntity : jitEntities) {
            if (provider.isCompatible(jitEntity)) {
              entity = jitEntity;
              found = true;
              break;
            }
          }
        }
        if (!found) {
          strategy.onFail(Notify.Failure.PROVIDER_NOT_APPLICABLE, text);
          appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
          continue;
        }
      }

      String value = provider.replaceUnchecked(handle, entity, placeholderString);

      if (value == null) {
        strategy.onFail(Notify.Failure.INVALID_RETURN_VALUE, text);
        appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
        continue;
      }

      // sets the placeholder value
      builder.append(value);
    }

    text = builder.toString();

    // TODO: Execute JIT Placeholder Providers before interceptors!
    return intercept(text);
  }

  private String intercept(String text) {
    for (MessageInterceptor interceptor : registry.getInterceptors()) {
      text = interceptor.intercept(text);
    }
    return text;
  }

  private void appendInvalidPlaceholder(StringBuilder builder, boolean identified, String identifier, String placeholder) {
    builder.append(startDelimiter);
    builder.append(identifier);
    if (identified) {
      builder.append(IDENTIFIER_SEPARATOR);
      builder.append(placeholder);
    }
    builder.append(endDelimiter);
  }

}
