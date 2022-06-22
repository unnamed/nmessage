package me.yushust.message.sponge.yaml;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;

public class YamlParse {

    public YamlParse() {
    }

    public ConfigurationNode fromFile(
            File file
    ) {
        try {
            return YamlConfigurationLoader.builder()
                    .file(file)
                    .build()
                    .load();
        } catch (ConfigurateException e) {
            throw new RuntimeException("Cannot load YamlConfiguration", e);
        }
    }
}
