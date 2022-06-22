package me.yushust.message.sponge.yaml;

import me.yushust.message.source.MessageSource;

import java.io.File;

/**
 * Facade that adds static factory
 * methods for creating {@link YamlMessageSource}
 */
public class SpongeYamlMessageAdapt {

    private SpongeYamlMessageAdapt() {
    }

    /**
     * Creates a new {@link YamlMessageSource} for the
     * specified {@code folder} and using the provided {@code fileFormat}
     * to get the filenames using its language
     */
    public static MessageSource newYamlSource(
            File folder, String fileFormat
    ) {
        return new YamlMessageSource(folder, fileFormat);
    }

    /**
     * Creates a new {@link YamlMessageSource} for the
     * specified {@code folder} and using the default file format
     * ("lang_%lang%.yml") to get the filenames using its language
     */

    public static MessageSource newYamlSource(File folder) {
        return newYamlSource(folder, "lang_%lang%.yml");
    }

}
