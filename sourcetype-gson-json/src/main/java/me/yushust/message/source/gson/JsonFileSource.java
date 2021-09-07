package me.yushust.message.source.gson;

import com.google.gson.JsonObject;
import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class JsonFileSource
  extends AbstractCachedFileSource<JsonObject>
  implements MessageSource {

  private final File folder;

  public JsonFileSource(File folder, String fileFormat) {
    super(fileFormat);
    this.folder = folder;
  }

  @Override
  protected @Nullable Object getValue(JsonObject source, String path) {
    return JsonParse.getValue(source, path, getSectionSeparator());
  }

  @Override
  protected @Nullable JsonObject getSource(String filename) {
    File file = new File(folder, filename);
    if (!file.exists()) {
      return null;
    } else {
      return JsonParse.fromFile(file);
    }
  }
}
