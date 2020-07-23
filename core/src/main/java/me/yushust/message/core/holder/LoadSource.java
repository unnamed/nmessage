package me.yushust.message.core.holder;

import java.io.File;

public final class LoadSource {

    private final ClassLoader classLoader;
    private final File folder;

    public LoadSource(ClassLoader classLoader, File folder) {
        this.classLoader = classLoader;
        this.folder = folder;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public File getFolder() {
        return folder;
    }

}
