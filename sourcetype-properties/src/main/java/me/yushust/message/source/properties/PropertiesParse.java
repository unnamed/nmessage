package me.yushust.message.source.properties;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Utility class that holds some utility
 * methods for parsing {@link Properties}
 * from {@link InputStream}s or {@link File}s
 */
public final class PropertiesParse {

    public static final Charset DEFAULT_CHARSET
            = StandardCharsets.ISO_8859_1;

    private PropertiesParse() {
    }

    /**
     * Creates a new {@link FileInputStream} for
     * the specified {@code file} and reads the
     * data. Calls {@link PropertiesParse#fromInputStream}
     * with the created {@link InputStream}
     *
     * @see PropertiesParse#fromInputStream
     */
    public static @Nullable Properties fromFile(File file, Charset charset) {
        try (InputStream input = new FileInputStream(file)) {
            return fromInputStream(input, charset);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read properties from file", e);
        }
    }

    /**
     * Reads and parses the data from the provided
     * {@code input} by using the {@link Properties#load}
     * method.
     *
     * <strong>This method doesn't close the
     * given {@code input} stream</strong>
     */
    public static @Nullable Properties fromInputStream(
            @Nullable InputStream input,
            Charset charset
    ) {
        if (input == null) {
            return null;
        }
        Properties properties = new Properties();
        // reader isn't closed so InputStream isn't
        Reader reader = new InputStreamReader(input, charset);
        try {
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load properties", e);
        }
        return properties;
    }

}
