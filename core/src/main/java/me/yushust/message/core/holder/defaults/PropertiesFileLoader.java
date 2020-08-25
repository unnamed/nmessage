package me.yushust.message.core.holder.defaults;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import me.yushust.message.core.holder.NodeFile;
import me.yushust.message.core.holder.NodeFileLoader;

import static java.util.Objects.requireNonNull;

public class PropertiesFileLoader implements NodeFileLoader {

    private final File folder;

    public PropertiesFileLoader(File folder) {
        this.folder = requireNonNull(folder);
    }

    @Override
    public NodeFile load(File file) throws IOException {
        
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
    public NodeFile loadAndCreate(InputStream inputStream, String fileName) throws IOException {

        Properties properties = new Properties();
        properties.load(inputStream);
        
        File file = new File(folder, fileName);

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
    
}