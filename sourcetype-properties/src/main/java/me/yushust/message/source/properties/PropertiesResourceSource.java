package me.yushust.message.source.properties;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Properties;

/**
 * Implementation of {@link MessageSource} for
 * {@link Properties} resources.
 *
 * <p>This implementation is <b>cached</b> and only
 * checks for {@link Properties} resources in the
 * {@code classLoader}</p>
 */
public class PropertiesResourceSource
  extends AbstractCachedFileSource<Properties>
  implements MessageSource {

  /** Class loader that holds the resources */
  private final ClassLoader classLoader;

  /**
   * Constructs a new properties message source
   * using the provided {@code classLoader} and
   * the specified {@code fileFormat}
   */
  public PropertiesResourceSource(
    ClassLoader classLoader,
    String fileFormat
  ) {
    super(fileFormat);
    Validate.isNotNull(classLoader, "classLoader");
    this.classLoader = classLoader;
  }

  /**
   * Constructs a nwe properties message source
   * using the {@link ClassLoader} for this {@link Class}
   * and the specified {@code fileFormat}
   */
  public PropertiesResourceSource(String fileFormat) {
    super(fileFormat);
    this.classLoader = getClass().getClassLoader();
  }

  @Override
  @Nullable
  protected Properties getSource(String filename) {
    InputStream input = classLoader.getResourceAsStream(filename);
    return PropertiesParse.fromInputStream(input);
  }

  @Override
  @Nullable
  protected Object getValue(Properties source, String path) {
    return source.get(path);
  }

}
