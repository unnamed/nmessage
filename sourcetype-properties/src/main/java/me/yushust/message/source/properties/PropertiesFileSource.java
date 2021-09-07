package me.yushust.message.source.properties;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.Charset;
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

  /** Charset to load properties */
  private final Charset charset;

  public PropertiesFileSource(
    File folder,
    String fileFormat,
    Charset charset
  ) {
    super(fileFormat);
    Validate.isNotNull(folder, "folder");
    Validate.isNotNull(charset, "charset");
    this.folder = folder;
    this.charset = charset;
    if (!folder.exists() && !folder.mkdirs()) {
      throw new IllegalStateException(
        "Cannot create container folder (" + folder.getName() + ')'
      );
    }
  }

  public PropertiesFileSource(
    File folder,
    String fileFormat
  ) {
    this(
      folder,
      fileFormat,
      PropertiesParse.DEFAULT_CHARSET
    );
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
      return PropertiesParse.fromFile(file, charset);
    }
  }

}
