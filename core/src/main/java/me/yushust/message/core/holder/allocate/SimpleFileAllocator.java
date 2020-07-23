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

    private final Map<String, NodeFile> loadedFiles = new HashMap<>();
    private final NodeFileLoader nodeFileLoader;
    private final LoadSource source;

    public SimpleFileAllocator(NodeFileLoader nodeFileLoader, LoadSource source) {
        this.nodeFileLoader = requireNonNull(nodeFileLoader);
        this.source = requireNonNull(source);
    }

    @Override
    public Optional<NodeFile> find(String language) {
        NodeFile file = loadedFiles.get(language);
        if (file == null) {
            file = createIfResourceIsPresent(language)
                    .orElseGet(() -> loadIfFileIsPresent(language).orElse(null));
        }
        return Optional.ofNullable(file);
    }

    @Override
    public Optional<NodeFile> createIfResourceIsPresent(String language) {
        try (InputStream resource = source.getClassLoader().getResourceAsStream(language)) {
            if (resource == null) {
                return Optional.empty();
            }
            NodeFile file = nodeFileLoader.load(resource);
            loadedFiles.put(language, file);
            return Optional.of(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<NodeFile> loadIfFileIsPresent(String language) {
        File file = new File(source.getFolder(), language);
        if (!file.exists()) {
            return Optional.empty();
        }
        try {
            NodeFile nodeFile = nodeFileLoader.load(file);
            loadedFiles.put(language, nodeFile);
            return Optional.of(nodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
