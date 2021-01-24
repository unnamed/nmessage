package me.yushust.message.format;

import me.yushust.message.track.TrackingContext;

public interface PlaceholderValueProvider {

  String getValue(
    TrackingContext context,
    String identifier,
    String parameters
  );

  String getValue(
    TrackingContext context,
    String identifier
  );

}

