package me.yushust.message.core.holder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Loads the NodeFiles with the specified
 * File or InputStream (resource)
 */
public interface NodeFileLoader {

    /**
     * Loads a new {@link NodeFile} using the
     * specified file
     * @param source The load source
     * @param file The file
     * @return The node file
     * @throws IOException Exception occurred while
     * loading or parsing the file, depending on
     * the implementation.
     */
    NodeFile load(LoadSource source, File file) throws IOException;

    /**
     * Loads a new {@link NodeFile} using the
     * specified {@link InputStream} and creates
     * a file with the content in the input stream
     * @param source The load source
     * @param inputStream The input stream
     * @param fileName The file name
     * @return The node file
     * @throws IOException Exception occurred while
     * loading or parsing the file, depending on
     * the implementation.
     */
    NodeFile loadAndCreate(LoadSource source, InputStream inputStream, String fileName) throws IOException;

}
