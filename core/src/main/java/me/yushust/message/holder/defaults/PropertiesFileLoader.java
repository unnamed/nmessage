package me.yushust.message.holder.defaults;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import me.yushust.message.holder.LoadSource;
import me.yushust.message.holder.NodeFile;
import me.yushust.message.holder.NodeFileLoader;

public class PropertiesFileLoader implements NodeFileLoader {

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
    
}