package me.yushust.message.format.bukkit.yaml;

import me.yushust.message.file.NodeFile;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlConfigurationWrapper implements NodeFile {

  private final YamlConfiguration config;

  public YamlConfigurationWrapper(YamlConfiguration config) {
    this.config = config;
  }

  @Override
  public Object get(String node) {
    return config.get(node);
  }
}
