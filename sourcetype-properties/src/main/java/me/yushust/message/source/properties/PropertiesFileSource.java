package me.yushust.message.source.properties;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileSource
  extends AbstractCachedFileSource
  implements MessageSource {

  private final File folder;

  public PropertiesFileSource(
    File folder,
    String fileFormat
  ) {
    super(fileFormat);
    this.folder = Validate.isNotNull(folder);
    if (!folder.exists()) {
      if (!folder.mkdirs()) {
        throw new IllegalStateException(
          "Cannot create container folder (" + folder.getName() + ')'
        );
      }
    }
  }

  @Override
  @Nullable
  protected Object getValue(Object source, String path) {
    return ((Properties) source).get(path);
  }

  @Override
  @Nullable
  protected Object getSource(String filename) {
    File file = new File(folder, filename);
    if (!file.exists()) {
      return null;
    } else {
      try (InputStream input = new FileInputStream(file)) {
        return PropertiesParse.fromInputStream(input);
      } catch (IOException e) {
        throw new IllegalStateException("Cannot load properties", e);
      }
    }
  }

}
