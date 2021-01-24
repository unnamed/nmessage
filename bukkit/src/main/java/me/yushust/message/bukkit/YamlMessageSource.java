package me.yushust.message.bukkit;

import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class YamlMessageSource
  extends AbstractYamlMessageSource
  implements MessageSource {

  private static final String LANGUAGE_VARIABLE = "%lang%";

  private final Plugin plugin;
  private final File folder;
  private final String fileFormat;

  public YamlMessageSource(
    Plugin plugin,
    File folder,
    String fileFormat
  ) {
    this.plugin = Validate.isNotNull(plugin, "plugin");
    this.folder = Validate.isNotNull(folder, "folder");
    this.fileFormat = Validate.isNotEmpty(fileFormat);

    Validate.isTrue(
      fileFormat.contains(LANGUAGE_VARIABLE),
      "File format (" + fileFormat + ") must contain " +
        "the language variable (" + LANGUAGE_VARIABLE + ')'
    );

    if (!folder.exists()) {
      if (!folder.mkdirs()) {
        throw new IllegalStateException(
          "Cannot create container folder (" + folder.getName() + ')'
        );
      }
    }
  }

  @Override
  public Object get(@Nullable String language, String path) {

    if (language == null) {
      return null;
    }

    Object value = cache.get(language);

    if (value == ABSENT) {
      return null;
    } else if (value == null) {
      String fileName = fileFormat.replace(LANGUAGE_VARIABLE, language);
      File file = new File(folder, fileName);

      if (file.exists()) {
        try {
          value = fromInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
          throw new IllegalStateException("Cannot load messages from file", e);
        }
      }

      if (value == null) {
        InputStream resource = plugin.getResource(fileName);

        if (resource == null) {
          return null;
        } else {
          YamlConfiguration yaml = fromInputStream(resource);
          value = yaml;

          try {
            if (!file.createNewFile()) {
              throw new RuntimeException("already created");
            }
            yaml.save(file);
          } catch (IOException e) {
            throw new IllegalStateException("Cannot create messages file", e);
          }
        }
      }
    }

    YamlConfiguration yaml = (YamlConfiguration) value;
    return yaml.get(path);
  }

}
