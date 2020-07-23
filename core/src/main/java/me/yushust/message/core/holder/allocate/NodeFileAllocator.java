package me.yushust.message.core.holder.allocate;

import me.yushust.message.core.holder.NodeFile;

import java.util.Optional;

public interface NodeFileAllocator {

    Optional<NodeFile> find(String language);

    Optional<NodeFile> createIfResourceIsPresent(String language);

    Optional<NodeFile> loadIfFileIsPresent(String language);

}
