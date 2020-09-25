package me.yushust.message.intercept;

import me.yushust.message.MessageHandler;
import me.yushust.message.generic.ResolvedPlaceholderProvider;
import me.yushust.message.placeholder.PlaceholderBox;
import me.yushust.message.provide.ProvideContext;
import me.yushust.message.util.Validate;

/**
 * {@inheritDoc}
 */
public class DefaultInterceptManager<E> extends DefaultFormatterRegistry<E> implements InterceptManager<E> {

  private final char identifierSeparator = '_';
  protected final PlaceholderBox placeholderBox;

  private MessageHandler<E> handle;

  public DefaultInterceptManager(Class<E> entityType, PlaceholderBox placeholderBox) {
    super(entityType);
    this.placeholderBox = Validate.notNull(placeholderBox);
  }

  public DefaultInterceptManager(Class<E> entityType) {
    super(entityType);
    this.placeholderBox = PlaceholderBox.DEFAULT;
  }

  @Override
  public void setHandle(MessageHandler<E> handle) {
    Validate.notNull(handle, "handle");
    Validate.state(this.handle == null, "The handle is already defined!");
    this.handle = handle;
  }

  @Override
  public String convert(ProvideContext<E> context, String text) {

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
        } else if (!identified && current == identifierSeparator) {
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
            .append(placeholderBox.getEnd())
            .append(identifierString);

        if (identified) {
          builder
              .append(identifierSeparator)
              .append(placeholderString);
        }
        continue;
      }

      ResolvedPlaceholderProvider<?> provider = getProvider(identifierString);
      if (provider == null || context.ignores(provider)) {
        appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
        continue;
      }

      Object entity = context.getEntity();

      if (!provider.accepts(entity)) {
        boolean found = false;
        for (Object jitEntity : context.getJitEntities()) {
          if (provider.accepts(jitEntity)) {
            entity = jitEntity;
            found = true;
            break;
          }
        }
        if (!found) {
          appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
          continue;
        }
      }

      ProvideContext<E> currentProviderContext = context.copy()
          .ignore(provider);

      String value = provider.replaceCasting(
          handle.withContext(currentProviderContext),
          entity,
          placeholderString
      );

      if (value == null) {
        appendInvalidPlaceholder(builder, identified, identifierString, placeholderString);
        continue;
      }

      // sets the placeholder value
      builder.append(value);
    }

    text = builder.toString();

    for (MessageInterceptor interceptor : getInterceptors()) {
      text = interceptor.intercept(text);
    }

    return text;
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
