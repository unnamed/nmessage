package me.yushust.message.test.mock;

import me.yushust.message.format.PlaceholderValueProvider;
import me.yushust.message.track.TrackingContext;

public class MockPlaceholderValueProvider
  implements PlaceholderValueProvider {

  @Override
  public String getValue(TrackingContext context, String identifier, String parameters) {
    return null;
  }

}
