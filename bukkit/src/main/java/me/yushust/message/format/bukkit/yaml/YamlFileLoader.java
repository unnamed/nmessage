package me.yushust.message.format.bukkit.yaml;

import me.yushust.message.source.LoadSource;
import me.yushust.message.source.NodeFile;
import me.yushust.message.source.NodeFileLoader;
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
    YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    return new YamlNodeFile(data);
  }

  @Override
  public NodeFile loadAndCreate(LoadSource source, InputStream inputStream, String fileName) {
    File file = new File(source.getFolder(), fileName);
    if (!file.exists()) {
      plugin.saveResource(fileName, false);
    }
    return load(source, file);
  }

  private static class YamlNodeFile implements NodeFile {

    private final YamlConfiguration data;

    private YamlNodeFile(YamlConfiguration data) {
      this.data = data;
    }

    @Override
    public Object get(String node) {
      return data.get(node);
    }

    @Override
    public String toString() {
      return "YamlNodeFile (*.yml)";
    }
  }

}
