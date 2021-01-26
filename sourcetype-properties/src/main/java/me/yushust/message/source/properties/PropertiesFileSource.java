package me.yushust.message.source.properties;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Properties;

/**
 * Implementation of {@link MessageSource} for
 * {@link Properties} files.
 *
 * <p>This implementation is <b>cached</b> and only
 * checks for {@link Properties} in the specified
 * {@code folder}</p>
 */
public class PropertiesFileSource
  extends AbstractCachedFileSource<Properties>
  implements MessageSource {

  /** The folder where the messages files are contained */
  private final File folder;

  public PropertiesFileSource(
    File folder,
    String fileFormat
  ) {
    super(fileFormat);
    this.folder = Validate.isNotNull(folder);
    if (!folder.exists() && !folder.mkdirs()) {
      throw new IllegalStateException(
        "Cannot create container folder (" + folder.getName() + ')'
      );
    }
  }

  @Override
  @Nullable
  protected Object getValue(Properties source, String path) {
    return source.get(path);
  }

  @Override
  @Nullable
  protected Properties getSource(String filename) {
    File file = new File(folder, filename);
    if (!file.exists()) {
      return null;
    } else {
      return PropertiesParse.fromFile(file);
    }
  }

}
