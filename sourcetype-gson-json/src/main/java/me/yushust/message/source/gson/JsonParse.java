package me.yushust.message.source.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Utility class for parsing
 * json using the Google GSON
 * library. This class just surrounds
 * some exceptions
 */
public final class JsonParse {

    private JsonParse() {
    }

    public static @Nullable String getValue(JsonObject source, String path, char separator) {
        StringBuilder builder = new StringBuilder();
        JsonObject checking = source;
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == separator) {
                String section = builder.toString();
                builder.setLength(0);
                checking = checking.getAsJsonObject(section);

                if (checking == null) {
                    // The section does not exist
                    return null;
                }
            } else {
                builder.append(c);
            }
        }

        JsonElement valueElement = checking.get(builder.toString());
        if (valueElement == null || valueElement.isJsonNull()) {
            return null;
        } else {
            return valueElement.getAsString();
        }
    }

    /**
     * Reads a json object from the given {@code file}
     */
    public static @Nullable JsonObject fromFile(File file) {
        try (InputStream input = new FileInputStream(file)) {
            return fromInputStream(input);
        } catch (IOException e) {
            throw new IllegalStateException("Error appeared while opening a file input stream", e);
        }
    }

    /**
     * Reads a json object from the given {@code input}
     */
    @Contract("null -> null")
    public static @Nullable JsonObject fromInputStream(@Nullable InputStream input) {
        if (input == null) {
            return null;
        }
        try (Reader reader = new InputStreamReader(input)) {
            JsonElement element = JsonParser.parseReader(reader);
            if (!element.isJsonObject()) {
                throw new IllegalArgumentException("Root must be a JSON object!");
            }
            return element.getAsJsonObject();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse JSON data from input stream", e);
        }
    }
}
