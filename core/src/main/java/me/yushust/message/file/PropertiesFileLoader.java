package me.yushust.message.file;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

enum PropertiesFileLoader implements NodeFileLoader {

  INSTANCE;

  @Override
  public NodeFile load(LoadSource source, File file) throws IOException {

    if (!file.exists()) {
      return null;
    }

    try (Reader reader = new FileReader(file)) {
      Properties properties = new Properties();
      properties.load(reader);
      return new PropertiesNodeFile(properties);
    }
  }

  @Override
  public NodeFile loadAndCreate(LoadSource source, InputStream inputStream, String fileName) throws IOException {

    Properties properties = new Properties();
    properties.load(inputStream);

    File file = new File(source.getFolder(), fileName);

    if (!file.exists()) {
      if (!file.createNewFile()) {
        throw new IOException("Cannot create file " + fileName);
      }
    }

    try (Writer writer = new FileWriter(file)) {
      properties.store(writer, null);
    }

    return new PropertiesNodeFile(properties);
  }

  static final class PropertiesNodeFile implements NodeFile {

    private final Properties properties;

    PropertiesNodeFile(Properties properties) {
      this.properties = requireNonNull(properties);
    }

    @Override
    public String get(String node) {
      return properties.getProperty(node, null);
    }

    @Override
    public String toString() {
      return "PropertiesNodeFile (*.properties)";
    }
  }
}