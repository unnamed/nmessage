package me.yushust.message.source.properties;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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

  /** Charset to read properties */
  private final Charset charset;

  /**
   * Constructs a new properties message source
   * using the {@code classLoader} and the {@code fileFormat},
   * will use the given {@code charset} to decode the data
   */
  public PropertiesResourceSource(
    ClassLoader classLoader,
    String fileFormat,
    Charset charset
  ) {
    super(fileFormat);
    Validate.isNotNull(classLoader, "classLoader");
    Validate.isNotNull(charset, "charset");
    this.classLoader = classLoader;
    this.charset = charset;
  }

  /**
   * Constructs a new properties message source
   * using the {@code classLoader} and the {@code fileFormat},
   * uses ISO-8859-1 charset by default
   */
  public PropertiesResourceSource(
    ClassLoader classLoader,
    String fileFormat
  ) {
    this(classLoader, fileFormat, PropertiesParse.DEFAULT_CHARSET);
  }

  /**
   * Constructs a nwe properties message source
   * using the {@link ClassLoader} for this {@link Class}
   * and the specified {@code fileFormat}
   */
  public PropertiesResourceSource(String fileFormat) {
    super(fileFormat);
    this.classLoader = getClass().getClassLoader();
    this.charset = PropertiesParse.DEFAULT_CHARSET;
  }

  @Override
  protected @Nullable Properties getSource(String filename) {
    try (InputStream input = classLoader.getResourceAsStream(filename)) {
      return PropertiesParse.fromInputStream(input, charset);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to close resource " + filename);
    }
  }

  @Override
  protected @Nullable Object getValue(Properties source, String path) {
    return source.get(path);
  }

}
