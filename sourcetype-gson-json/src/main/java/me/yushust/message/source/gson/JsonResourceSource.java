package me.yushust.message.source.gson;

import com.google.gson.JsonObject;
import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class JsonResourceSource
  extends AbstractCachedFileSource<JsonObject>
  implements MessageSource {

  private final ClassLoader classLoader;

  public JsonResourceSource(ClassLoader classLoader, String fileFormat) {
    super(fileFormat);
    this.classLoader = classLoader;
  }

  public JsonResourceSource(String fileFormat) {
    this(
      JsonResourceSource.class.getClassLoader(),
      fileFormat
    );
  }

  @Override
  protected @Nullable Object getValue(JsonObject source, String path) {
    return JsonParse.getValue(source, path, getSectionSeparator());
  }

  @Override
  protected @Nullable JsonObject getSource(String filename) {
    try (InputStream input = classLoader.getResourceAsStream(filename)) {
      return JsonParse.fromInputStream(input);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to close resource " + filename);
    }
  }
}
