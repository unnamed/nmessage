package me.yushust.message.source.gson;

import com.google.gson.JsonObject;
import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import org.jetbrains.annotations.Nullable;

public class PropertiesResourceSource
  extends AbstractCachedFileSource<JsonObject>
  implements MessageSource {

  private final ClassLoader classLoader;

  public PropertiesResourceSource(ClassLoader classLoader, String fileFormat) {
    super(fileFormat);
    this.classLoader = classLoader;
  }

  public PropertiesResourceSource(String fileFormat) {
    this(
      PropertiesResourceSource.class.getClassLoader(),
      fileFormat
    );
  }

  @Override
  protected @Nullable Object getValue(JsonObject source, String path) {
    return JsonParse.getValue(source, path, getSectionSeparator());
  }

  @Override
  protected @Nullable JsonObject getSource(String filename) {
    return JsonParse.fromInputStream(
      classLoader.getResourceAsStream(filename)
    );
  }
}
