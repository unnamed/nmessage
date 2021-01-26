package me.yushust.message.source.properties;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesResourceSource
  extends AbstractCachedFileSource
  implements MessageSource {

  private final ClassLoader classLoader;

  public PropertiesResourceSource(
    ClassLoader classLoader,
    String fileFormat
  ) {
    super(fileFormat);
    Validate.isNotNull(classLoader, "classLoader");
    this.classLoader = classLoader;
  }

  public PropertiesResourceSource(String fileFormat) {
    super(fileFormat);
    this.classLoader = getClass().getClassLoader();
  }

  @Override
  @Nullable
  protected Object getSource(String filename) {
    InputStream input = classLoader.getResourceAsStream(filename);
    return PropertiesParse.fromInputStream(input);
  }

  @Override
  @Nullable
  protected Object getValue(Object source, String path) {
    return ((Properties) source).get(path);
  }

}
