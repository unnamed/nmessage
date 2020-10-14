package me.yushust.message.internal;

import me.yushust.message.FailureListener;
import me.yushust.message.MessageInterceptor;
import me.yushust.message.Notify;
import me.yushust.message.PlaceholderBox;
import me.yushust.message.util.Validate;

final class PlaceholderReplacer {

  private static final char IDENTIFIER_SEPARATOR = '_';

  private final PlaceholderBox delimiters;
  private final MessageHandlerImpl<?> handle;
  private final FormatterRegistry<?> registry;
  private final FailureListener failureListener;

  PlaceholderReplacer(MessageHandlerImpl<?> handle, PlaceholderBox delimiters) {
    this.handle = handle;
    this.delimiters = delimiters;
    this.registry = handle.getFormatterRegistry();
    this.failureListener = handle.getFailureListener();
  }

  String replace(Object entity, String text, Object... jitEntities) {

    Validate.notNull(text);

    // The minimum length to replace placeholders is 3,
    // because "%_%" is a valid placeholder
    if (text.length() < 4) {
      return text;
    }

    // I don't use text.toCharArray() because it copies
    // the entire internal array
    StringBuilder builder = new StringBuilder(text.length());
    StringBuilder identifier = new StringBuilder();
    StringBuilder placeholder = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {

      char character = text.charAt(i);

      if (character != delimiters.getStart() || i + 1 >= text.length()) {
        builder.append(character);
        continue;
      }

      boolean closed = false;
      boolean identified = false;

      while (++i < text.length()) {

        char current = text.charAt(i);

        if (current == delimiters.getEnd()) {
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
            .append(delimiters.getEnd())
            .append(identifierString);

        if (identified) {
          builder
              .append(delimiters)
              .append(placeholderString);
        }
        continue;
      }

      ResolvedPlaceholderProvider<?> provider = registry.getProvider(identifierString);

      if (provider == null) {
        appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
        failureListener.onFail(Notify.Failure.PROVIDER_NOT_FOUND, text);
        continue;
      }

      if (!provider.accepts(entity)) {
        boolean found = false;
        for (Object jitEntity : jitEntities) {
          if (provider.accepts(jitEntity)) {
            entity = jitEntity;
            found = true;
            break;
          }
        }
        if (!found) {
          failureListener.onFail(Notify.Failure.PROVIDER_NOT_APPLICABLE, text);
          appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
          continue;
        }
      }

      String value = provider.replaceCasting(handle, entity, placeholderString);

      if (value == null) {
        failureListener.onFail(Notify.Failure.INVALID_RETURN_VALUE, text);
        appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
        continue;
      }

      // sets the placeholder value
      builder.append(value);
    }

    text = builder.toString();

    // TODO: Execute JIT Placeholder Providers before interceptors!

    for (MessageInterceptor interceptor : registry.getInterceptors()) {
      text = interceptor.intercept(text);
    }

    return text;
  }

  private void appendInvalidPlaceholder(StringBuilder builder, boolean identified, String identifier, String placeholder) {
    builder.append(delimiters.getStart());
    builder.append(identifier);
    if (identified) {
      builder.append(IDENTIFIER_SEPARATOR);
      builder.append(placeholder);
    }
    builder.append(delimiters.getEnd());
  }

}
