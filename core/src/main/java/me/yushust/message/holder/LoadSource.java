package me.yushust.message.holder;

import java.io.File;

/**
 * Represents a load source (from where the files
 * will be loaded to get the messages)
 */
public final class LoadSource {

    private final ClassLoader classLoader;
    private final File folder;

    /**
     * Constructs a new load source.
     * @param classLoader The class loader that will be used
     *                    to load the files from resources
     * @param folder The messages-files-folder that will be
     *               used to load the files present in that
     *               folder.
     */
    public LoadSource(ClassLoader classLoader, File folder) {
        this.classLoader = classLoader;
        this.folder = folder;
    }

    /**
     * @return The class loader, used to load the files
     * from resources
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * @return The messages files folder, used to load
     * the files present in that folder
     */
    public File getFolder() {
        return folder;
    }

}
