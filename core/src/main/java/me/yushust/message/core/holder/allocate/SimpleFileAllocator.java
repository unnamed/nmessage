package me.yushust.message.core.holder.allocate;

import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFile;
import me.yushust.message.core.holder.NodeFileLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class SimpleFileAllocator implements NodeFileAllocator {

    private static final Object UNDEFINED = new Object();

    private final Map<String, Object> loadedFiles = new HashMap<>();
    private final NodeFileLoader nodeFileLoader;
    private final LoadSource source;

    public SimpleFileAllocator(NodeFileLoader nodeFileLoader, LoadSource source) {
        this.nodeFileLoader = requireNonNull(nodeFileLoader);
        this.source = requireNonNull(source);
    }

    @Override
    public Optional<NodeFile> find(String filename) {

        requireNonNull(filename);

        if (!this.shouldLoad(filename)) {
            return this.getCachedFile(filename);
        }

        return Optional.ofNullable(this.createIfResourceIsPresent(filename)
            .orElseGet(
                    () -> this.loadIfFileIsPresent(filename)
                            .orElse(null)
            ));
    }

    @Override
    public Optional<NodeFile> createIfResourceIsPresent(String filename) {

        requireNonNull(filename);

        if (!this.shouldLoad(filename)) {
            return this.getCachedFile(filename);
        }

        try (InputStream resource = this.source.getClassLoader().getResourceAsStream(filename)) {

            if (resource == null) {
                this.loadedFiles.put(filename, UNDEFINED);
                return Optional.empty();
            }

            NodeFile file = this.nodeFileLoader.loadAndCreate(resource, filename);
            this.loadedFiles.put(filename, file);
            return Optional.of(file);

        } catch (IOException e) {
            this.loadedFiles.put(filename, UNDEFINED);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<NodeFile> loadIfFileIsPresent(String filename) {

        requireNonNull(filename);

        if (!this.shouldLoad(filename)) {
            return this.getCachedFile(filename);
        }

        File file = new File(this.source.getFolder(), filename);

        if (!file.exists()) {
            this.loadedFiles.put(filename, UNDEFINED);
            return Optional.empty();
        }

        try {
            NodeFile nodeFile = this.nodeFileLoader.load(file);
            this.loadedFiles.put(filename, nodeFile);
            return Optional.of(nodeFile);
        } catch (IOException e) {
            this.loadedFiles.put(filename, UNDEFINED);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<NodeFile> getCachedFile(String filename) {

        if (this.shouldLoad(filename)) {
            return Optional.empty();
        }

        Object value = this.loadedFiles.get(filename);

        if (value == UNDEFINED) {
            return Optional.empty();
        }

        return Optional.of((NodeFile) value);
    }

    private boolean shouldLoad(String filename) {
        Object value = this.loadedFiles.get(filename);

        if (value == UNDEFINED) {
            return false;
        }

        if (value != null) {
            if (!(value instanceof NodeFile)) {
                throw new IllegalStateException(
                        "A object that isn't 'UNDEFINED' nor instance of NodeFile is in the loaded files map"
                );
            }
            return false;
        }

        return true;
    }

}
