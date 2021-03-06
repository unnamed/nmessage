package me.yushust.message.format;

import me.yushust.message.config.ConfigurationHandle;
import me.yushust.message.impl.TypeSpecificPlaceholderProvider;
import me.yushust.message.track.TrackingContext;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Default implementation of {@link PlaceholderValueProvider}
 * that checks for {@link PlaceholderProvider} to get values
 * for placeholders.
 *
 * <p>Also checks in {@link TrackingContext#getVariableReplacements}
 * to get the variable replacements when there are no placeholder
 * parameters.</p>
 */
public class PlaceholderValueProviderImpl
  implements PlaceholderValueProvider {

  private final ConfigurationHandle configuration;

  public PlaceholderValueProviderImpl(
    ConfigurationHandle configuration
  ) {
    this.configuration = configuration;
  }

  @Override
  @Nullable
  public String getValue(
    TrackingContext context,
    String identifier,
    String parameters
  ) {
    TypeSpecificPlaceholderProvider<?> provider =
      configuration.getProvider(identifier);
    Object value;

    if (provider == null) {
      value = null;
    } else {
      Object entity = context.getEntity();
      if (!provider.isCompatible(entity)) {
        entity = null;
        for (Object ext : context.getJitEntities()) {
          if (provider.isCompatible(ext)) {
            entity = ext;
            break;
          }
        }
      }

      if (provider.isCompatible(entity)) {
        value = provider.replaceUnchecked(
          context.getContextRepository(),
          entity,
          parameters
        );
      } else {
        value = null;
      }
    }

    return convertObjectToString(null, value);
  }

  @Override
  @Nullable
  public String getValue(
    TrackingContext context,
    String identifier
  ) {
    Map<String, Object> replacements = context.getVariableReplacements();
    Object value = replacements == null ? null : replacements.get(identifier);
    return convertObjectToString(null, value);
  }

}
