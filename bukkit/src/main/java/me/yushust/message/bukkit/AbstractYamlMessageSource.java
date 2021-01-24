package me.yushust.message.bukkit;

import me.yushust.message.source.MessageSource;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractYamlMessageSource
  implements MessageSource {

  /**
   * Sentinel value that indicates that
   * a message source isn't present for
   * a specific language
   */
  protected static final Object ABSENT =
    new Object();

  /**
   * Map that contains the stored
   * results while obtaining a message
   * source
   */
  protected final Map<String, Object> cache
    = new HashMap<>();

  /**
   * Reads the data in the {@code input} stream
   * and creates a new {@link YamlConfiguration} instance
   */
  protected final YamlConfiguration fromInputStream(
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

  /**
   * Gets the value linked to the specified
   * {@code language} from the {@code cache} map.
   *
   * <p>Replaces the sentinel value {@code ABSENT}
   * with NULL</p>
   */
  protected final YamlConfiguration getMemoizedConfig(
    String language
  ) {
    Object value = cache.get(language);
    if (value == ABSENT) {
      return null;
    } else {
      return (YamlConfiguration) value;
    }
  }

}
