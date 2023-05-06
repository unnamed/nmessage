package me.yushust.message.source.configurate.yml;

import me.yushust.message.source.MessageSource;
import java.io.File;

/**
 * Facade that adds static factory
 * methods for creating {@link ConfigurateYamlMessageSource}
 */
public class ConfigurateYamlMessageAdapt {

    private ConfigurateYamlMessageAdapt() {}

    /**
     * Creates a new {@link ConfigurateYamlMessageSource} for the
     * specified {@code folder} and using the provided {@code fileFormat}
     * to get the filenames using its language
     */
    public static MessageSource newYamlSource(File folder, String fileFormat) {
        return new ConfigurateYamlMessageSource(folder, fileFormat);
    }

    /**
     * Creates a new {@link ConfigurateYamlMessageSource} for the
     * specified {@code folder} and using the default file format
     * ("lang_%lang%.yml") to get the filenames using its language
     */

    public static MessageSource newYamlSource(File folder) {
        return newYamlSource(folder, "lang_%lang%.yml");
    }

}