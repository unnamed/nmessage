package me.yushust.message.format.bukkit.yaml;

import me.yushust.message.core.holder.NodeFile;
import me.yushust.message.core.holder.NodeFileLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class YamlFileLoader implements NodeFileLoader {

    private final Plugin plugin;
    private final File folder;

    public YamlFileLoader(Plugin plugin, File folder) {
        this.plugin = requireNonNull(plugin);
        this.folder = requireNonNull(folder);
    }

    @Override
    public NodeFile load(File file) {
        return new YamlConfigurationWrapper(
                YamlConfiguration.loadConfiguration(file)
        );
    }

    @Override
    public NodeFile loadAndCreate(InputStream inputStream, String fileName) {
        File file = new File(folder, fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        return load(file);
    }

}
