package me.yushust.message.holder.allocate;

import me.yushust.message.holder.LoadSource;
import me.yushust.message.holder.NodeFile;
import me.yushust.message.holder.NodeFileLoader;

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

        Object value = loadedFiles.get(filename);

        if (value == UNDEFINED) {
            return Optional.empty();
        }

        if (value != null) {
            if (!(value instanceof NodeFile)) {
                throw new IllegalStateException(
                        "A object that isn't 'UNDEFINED' nor instance of NodeFile is in the loaded files map"
                );
            }
            return Optional.of((NodeFile) value);
        }

        Optional<NodeFile> nodeFile = this.createIfResourceIsPresent(filename);

        if (!nodeFile.isPresent()) {
            nodeFile = this.loadIfFileIsPresent(filename);
            if (!nodeFile.isPresent()) {
                loadedFiles.put(filename, UNDEFINED);
            }
        }

        return nodeFile;
    }

    @Override
    public Optional<NodeFile> createIfResourceIsPresent(String filename) {

        requireNonNull(filename);

        try (InputStream resource = this.source.getClassLoader().getResourceAsStream(filename)) {

            if (resource == null) {
                return Optional.empty();
            }

            NodeFile file = this.nodeFileLoader.loadAndCreate(source, resource, filename);
            this.loadedFiles.put(filename, file);
            return Optional.of(file);

        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<NodeFile> loadIfFileIsPresent(String filename) {

        requireNonNull(filename);

        File file = new File(this.source.getFolder(), filename);

        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            NodeFile nodeFile = this.nodeFileLoader.load(source, file);
            this.loadedFiles.put(filename, nodeFile);
            return Optional.of(nodeFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}