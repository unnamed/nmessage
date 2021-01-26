package me.yushust.message.bukkit;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class YamlParse {

  private YamlParse() {
  }

  /**
   * Reads the data in the {@code input} stream
   * and creates a new {@link YamlConfiguration} instance
   */
  public static YamlConfiguration fromInputStream(
    InputStream input
  ) {
    YamlConfiguration config = new YamlConfiguration();
    try (InputStreamReader reader = new InputStreamReader(input)) {
      config.load(reader);
    } catch (IOException | InvalidConfigurationException e) {
      throw new IllegalStateException("Cannot load YamlConfiguration", e);
    }
    return config;
  }

}
