package me.yushust.message.format;

import me.yushust.message.track.TrackingContext;
import org.jetbrains.annotations.Nullable;

/**
 * Responsible of obtaining the value
 * for the given placeholders. The default
 * implementation searches for the {@link PlaceholderProvider}
 * linked to the specified {@code identifier}
 */
public interface PlaceholderValueProvider {

  /**
   * Gets the value for a placeholder with
   * the specified {@code identifier} and
   * its {@code parameters}.
   */
  @Nullable
  String getValue(
    TrackingContext context,
    String identifier,
    String parameters
  );

  /**
   * Gets the value for a placeholder with
   * the specified {@code identifier}, this
   * method doesn't receive {@code parameters}
   */
  @Nullable
  default String getValue(
    TrackingContext context,
    String identifier
  ) {
    return null;
  }

}

