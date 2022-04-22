package me.yushust.message.bungee;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class YamlMessageSource
  extends AbstractCachedFileSource<Configuration>
  implements MessageSource {

  private final Plugin plugin;
  private final File folder;

  public YamlMessageSource(
    Plugin plugin,
    File folder,
    String fileFormat
  ) {
    super(fileFormat);
    this.plugin = Validate.isNotNull(plugin, "plugin");
    this.folder = Validate.isNotNull(folder, "folder");

    if (!folder.exists() && !folder.mkdirs()) {
      throw new IllegalStateException(
        "Cannot create container folder (" + folder.getName() + ')'
      );
    }
  }

  private @Nullable Configuration loadImpl(File file, String filename) {

    if (file.exists()) {
      try {
        return YamlParse.fromInputStream(new FileInputStream(file));
      } catch (FileNotFoundException e) {
        throw new IllegalStateException("Cannot load messages from file", e);
      }
    }

    InputStream resource = plugin.getResourceAsStream(filename);

    if (resource == null) {
      return null;
    } else {
      Configuration yaml = YamlParse.fromInputStream(resource);
      try {
        if (!file.createNewFile()) {
          throw new RuntimeException("already created");
        }

        ConfigurationProvider.getProvider(YamlConfiguration.class).save(yaml, file);

        return yaml;
      } catch (IOException e) {
        throw new IllegalStateException("Cannot create messages file", e);
      }
    }
  }

  @Override
  public void load(String language) {
    String filename = getFilename(language);
    File file = new File(folder, filename);
    Configuration config = loadImpl(file, filename);
    cache.put(language, config);
  }

  @Override
  protected @Nullable Configuration getSource(String filename) {
    File file = new File(folder, filename);
    return loadImpl(file, filename);
  }

  @Override
  protected @Nullable Object getValue(Configuration source, String path) {
    return source.get(path);
  }

}
