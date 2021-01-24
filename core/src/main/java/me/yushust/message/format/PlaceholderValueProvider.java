package me.yushust.message.format;

import me.yushust.message.track.TrackingContext;

public interface PlaceholderValueProvider {

  String getValue(
    TrackingContext context,
    String identifier,
    String parameters
  );

  default String getValue(
    TrackingContext context,
    String identifier
  ) {
    return null;
  }

}

