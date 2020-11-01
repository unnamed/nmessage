package me.yushust.message.format.bukkit.yaml;

import me.yushust.message.file.LoadSource;
import me.yushust.message.file.NodeFile;
import me.yushust.message.file.NodeFileLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class YamlFileLoader implements NodeFileLoader {

  private final Plugin plugin;

  public YamlFileLoader(Plugin plugin) {
    this.plugin = requireNonNull(plugin);
  }

  @Override
  public NodeFile load(LoadSource source, File file) {
    return new YamlConfigurationWrapper(
        YamlConfiguration.loadConfiguration(file)
    );
  }

  @Override
  public NodeFile loadAndCreate(LoadSource source, InputStream inputStream, String fileName) {
    File file = new File(source.getFolder(), fileName);
    if (!file.exists()) {
      plugin.saveResource(fileName, false);
    }
    return load(source, file);
  }

}
