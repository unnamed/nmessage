package me.yushust.message.source.configurate.yml;

import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import me.yushust.message.util.Validate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigurateYamlMessageSource
        extends AbstractCachedFileSource<ConfigurationNode>
        implements MessageSource {

    private final File folder;

    public ConfigurateYamlMessageSource(File folder, String fileFormat) {
        super(fileFormat);
        this.folder = Validate.isNotNull(folder, "folder");

        if (!folder.exists() && !folder.mkdirs()) {
            throw new IllegalStateException("Cannot create container folder (" + folder.getName() + ')');
        }
    }

    private @Nullable ConfigurationNode loadImpl(File file, String filename) {
        if (file.exists()) {
            return YamlParse.fromFile(file);
        }

        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (resource == null) {
                return null;
            }

            try (InputStream stream = new BufferedInputStream(resource)) {
                Files.copy(stream, file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return YamlParse.fromFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load(String language) {
        String filename = getFilename(language);
        File file = new File(folder, filename);
        ConfigurationNode config = loadImpl(file, filename);
        cache.put(language, config);
    }

    @Override
    protected @Nullable ConfigurationNode getSource(String filename) {
        File file = new File(folder, filename);
        return loadImpl(file, filename);
    }

    @Override
    protected @Nullable Object getValue(ConfigurationNode source, String path) {
        try {
            return source.node((Object[]) path.split("\\.")).get(Object.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

}