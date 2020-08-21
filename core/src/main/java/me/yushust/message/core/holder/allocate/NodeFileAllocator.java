package me.yushust.message.core.holder.allocate;

import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFile;
import me.yushust.message.core.holder.NodeFileLoader;

import java.util.Optional;

/**
 * Uses {@link LoadSource} and {@link NodeFileLoader} to
 * load {@link NodeFile}s
 */
public interface NodeFileAllocator {

    /**
     * Finds a {@link NodeFile} in cache
     * @param filename The file name
     * @return The node file wrapped with
     * Optional.
     */
    Optional<NodeFile> find(String filename);

    /**
     * Loads a {@link NodeFile} from a resource
     * @param resource The resource name
     * @return The node file wrapped with
     * Optional.
     */
    Optional<NodeFile> createIfResourceIsPresent(String resource);

    /**
     * Loads a {@link NodeFile} from a file
     * @param filename The file name
     * @return The node file wrapped with
     * Optional.
     */
    Optional<NodeFile> loadIfFileIsPresent(String filename);

}
