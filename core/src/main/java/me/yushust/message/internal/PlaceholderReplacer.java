package me.yushust.message.internal;

import me.yushust.message.MessageInterceptor;
import me.yushust.message.config.WiringContainer;

public final class PlaceholderReplacer {

  private final WiringContainer wiringContainer;
  private final String startDelimiter;
  private final String endDelimiter;

  public PlaceholderReplacer(
      WiringContainer wiringContainer,
      String startDelimiter,
      String endDelimiter
  ) {
    this.wiringContainer = wiringContainer;
    this.startDelimiter = startDelimiter;
    this.endDelimiter = endDelimiter;
  }

  public String executeInterceptors(String text) {
    for (MessageInterceptor interceptor : wiringContainer.getInterceptors()) {
      text = interceptor.intercept(text);
    }
    return text;
  }

  public String format(
      InternalContext context,
      String text,
      Object[] jitEntities
  ) {
    text = setPlaceholders(context, text, jitEntities);
    text = executeInterceptors(text);
    return text;
  }

  public String setPlaceholders(
      InternalContext context,
      String text,
      Object[] jitEntities
  ) {
    if (
        startDelimiter.isEmpty()
        || endDelimiter.isEmpty()
        || text.length() <
            3 + startDelimiter.length()
            + endDelimiter.length()
    ) {
      return text;
    }

    StringBuilder result = new StringBuilder();
    StringBuilder identifier = new StringBuilder();
    StringBuilder placeholder = new StringBuilder();

    int cursor = 0;
    boolean readingPlaceholder = false;
    boolean identified = false;
    String delimiter = startDelimiter;

    for (int i = 0; i < text.length(); i++) {
      char current = text.charAt(i);

      char expectedDelimiter = delimiter.charAt(cursor);
      if (current == expectedDelimiter) {
        cursor++;
      } else {
        if (readingPlaceholder) {
          if (identified) {
            placeholder.append(current);
          } else {
            if (current == '_') {
              identified = true;
            } else {
              identifier.append(current);
            }
          }
        } else {
          result.append(current);
        }
        cursor = 0;
      }

      if (cursor >= delimiter.length()) {
        if (readingPlaceholder) {

          String identifierStr = identifier.toString();
          String placeholderStr = placeholder.toString();
          String value;

          if (
              identifierStr.isEmpty()
                  || placeholderStr.isEmpty()
                  || (value = wiringContainer.getValue(
                      context,
                      identifierStr,
                      placeholderStr,
                      jitEntities
                  )) == null
          ) {
            result.append(startDelimiter);
            if (identified) {
              result.append("_");
            }
          } else {
            result.append(value);
          }

          placeholder.setLength(0);
          identifier.setLength(0);

          identified = false;
          readingPlaceholder = false;
          delimiter = startDelimiter;
        } else {
          delimiter = endDelimiter;
          readingPlaceholder = true;
        }
        cursor = 0;
      }
    }

    if (identifier.length() > 0) {
      result.append(startDelimiter);
      result.append(identifier);
    }

    if (placeholder.length() > 0) {
      if (identified) {
        result.append("_");
      }
      result.append(placeholder);
    }

    return result.toString();
  }

}
