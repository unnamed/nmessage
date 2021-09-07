package me.yushust.message.source.properties;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Utility class that holds some utility
 * methods for parsing {@link Properties}
 * from {@link InputStream}s or {@link File}s
 */
public final class PropertiesParse {

  private PropertiesParse() {
  }

  /**
   * Creates a new {@link FileInputStream} for
   * the specified {@code file} and reads the
   * data. Calls {@link PropertiesParse#fromInputStream}
   * with the created {@link InputStream}
   * @see PropertiesParse#fromInputStream
   */
  @Nullable
  public static Properties fromFile(File file) {
    try (InputStream input = new FileInputStream(file)) {
      return fromInputStream(input);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read properties from file", e);
    }
  }

  /**
   * Reads and parses the data from the provided
   * {@code input} by using the {@link Properties#load}
   * method.
   *
   * <strong>This method doesn't close the
   * given {@code input} stream</strong>
   */
  @Nullable
  public static Properties fromInputStream(
    @Nullable InputStream input
  ) {
    if (input == null) {
      return null;
    }
    Properties properties = new Properties();
    // reader isn't closed so InputStream isn't
    Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
    try {
      properties.load(reader);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot load properties", e);
    }
    return properties;
  }

}
