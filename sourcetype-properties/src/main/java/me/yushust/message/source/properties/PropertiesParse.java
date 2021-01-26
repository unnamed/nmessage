package me.yushust.message.source.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesParse {

  private PropertiesParse() {
  }

  public static Properties fromInputStream(
    InputStream input
  ) {
    if (input == null) {
      return null;
    }
    Properties properties = new Properties();
    try {
      properties.load(input);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot load properties", e);
    }
    return properties;
  }

}
