package me.yushust.message.format.bukkit.yaml;

import me.yushust.message.file.NodeFile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class YamlConfigurationWrapper implements NodeFile {

  private final YamlConfiguration config;

  public YamlConfigurationWrapper(YamlConfiguration config) {
    this.config = config;
  }

  @Override
  public String getString(String node) {
    return config.getString(node);
  }

  @Override
  public List<String> getStringList(String node) {
    return config.getStringList(node);
  }
}
