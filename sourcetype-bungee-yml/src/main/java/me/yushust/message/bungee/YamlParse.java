package me.yushust.message.bungee;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class YamlParse {

    private YamlParse() {
    }

    /**
     * Reads the data in the {@code input} stream
     * and creates a new {@link Configuration} instance
     */
    public static Configuration fromInputStream(
            InputStream input
    ) {
        Configuration configuration;

        try (InputStreamReader reader = new InputStreamReader(input)) {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load YamlConfiguration", e);
        }

        return configuration;
    }


}
