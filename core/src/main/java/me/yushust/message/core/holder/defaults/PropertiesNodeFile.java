package me.yushust.message.core.holder.defaults;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import me.yushust.message.core.holder.NodeFile;

import static java.util.Objects.requireNonNull;

import java.util.Collections;

public class PropertiesNodeFile implements NodeFile {

    private final Properties properties;

    public PropertiesNodeFile(Properties properties) {
        this.properties = requireNonNull(properties);
    }

    @Override
    public Optional<String> getString(String node) {
        return Optional.ofNullable(
            properties.getProperty(node, null)
        );
    }

    @Override
    public List<String> getStringList(String node) {
        return Collections.emptyList();
    }
    
}