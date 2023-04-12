package me.yushust.message.source.sponge.yml;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;

public class YamlParse {

    private YamlParse() {}

    public static ConfigurationNode fromFile(File file) {
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