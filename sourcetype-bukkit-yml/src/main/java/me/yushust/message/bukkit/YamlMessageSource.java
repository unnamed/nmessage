package me.yushust.message.bukkit;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class YamlMessageSource
        extends AbstractCachedFileSource<YamlConfiguration>
        implements MessageSource {

    private final Plugin plugin;
    private final File folder;

    public YamlMessageSource(
            Plugin plugin,
            File folder,
            String fileFormat
    ) {
        super(fileFormat);
        this.plugin = Validate.isNotNull(plugin, "plugin");
        this.folder = Validate.isNotNull(folder, "folder");

        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new IllegalStateException(
                        "Cannot create container folder (" + folder.getName() + ')'
                );
            }
        }
    }

    private @Nullable YamlConfiguration loadImpl(File file, String filename) {

        if (file.exists()) {
            try {
                return YamlParse.fromInputStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("Cannot load messages from file", e);
            }
        }

        InputStream resource = plugin.getResource(filename);

        if (resource == null) {
            return null;
        } else {
            YamlConfiguration yaml = YamlParse.fromInputStream(resource);
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("already created");
                }
                yaml.save(file);
                return yaml;
            } catch (IOException e) {
                throw new IllegalStateException("Cannot create messages file", e);
            }
        }
    }

    @Override
    public void load(String language) {
        String filename = getFilename(language);
        File file = new File(folder, filename);
        YamlConfiguration config = loadImpl(file, filename);
        cache.put(language, config);
    }

    @Override
    protected @Nullable YamlConfiguration getSource(String filename) {
        File file = new File(folder, filename);
        return loadImpl(file, filename);
    }

    @Override
    protected @Nullable Object getValue(YamlConfiguration source, String path) {
        return source.get(path);
    }

}
