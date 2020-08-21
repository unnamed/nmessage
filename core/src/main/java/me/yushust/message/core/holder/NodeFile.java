package me.yushust.message.core.holder;

import java.util.List;
import java.util.Optional;

/**
 * Represents a loaded path/node-based file
 * (configurations that used paths like
 * "path.other-path.etc" to locate something)
 * Can be YAML, JSON, PROPERTIES files, etc.
 */
public interface NodeFile {

    /**
     * Finds the text present in the path
     * @param node The path/node
     * @return The text wrapped in Optional
     */
    Optional<String> getString(String node);

    /**
     * Finds a list of text present in the
     * specified path/node
     * @param node The path/node
     * @return The found text list
     * @implNote Please return a empty list
     * instead of a null reference.
     */
    List<String> getStringList(String node);

}
