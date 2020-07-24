package me.yushust.message.format.bukkit.yaml;

import me.yushust.message.core.holder.NodeFile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Optional;

public class YamlConfigurationWrapper implements NodeFile {

    private final YamlConfiguration config;

    public YamlConfigurationWrapper(YamlConfiguration config) {
        this.config = config;
    }

    @Override
    public Optional<String> getString(String node) {
        return Optional.ofNullable(
                config.getString(node)
        );
    }

    @Override
    public List<String> getStringList(String node) {
        return config.getStringList(node);
    }
}
