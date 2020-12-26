package me.yushust.message.allocate;

import me.yushust.message.file.LoadSource;
import me.yushust.message.file.NodeFile;
import me.yushust.message.file.NodeFileLoader;
import me.yushust.message.util.Validate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of {@link NodeFilePool} that
 * tries to load the messages from a file and then tries
 * to load the messages from the classLoader resources.
 *
 * <p>The search of files and resources is executed only
 * once, then, a sentinel object is stored in the map,
 * marking the file as "searched but not found", and it
 * doesn't try to load it again</p>
 */
final class NodeFilePoolImpl implements NodeFilePool {

  /**
   * Sentinel value that marks that a file has been searched,
   * but not found. So it must not be searched again.
   */
  private static final Object ABSENT = new Object();

  private final Map<String, Object> loadedFiles = new HashMap<>();
  private final NodeFileLoader nodeFileLoader;
  private final LoadSource source;

  /**
   * Constructs a new pool using the specified {@code nodeFileLoader}
   * and {@code source} to load from
   */
  NodeFilePoolImpl(NodeFileLoader nodeFileLoader, LoadSource source) {
    this.nodeFileLoader = Validate.notNull(nodeFileLoader, "nodeFileLoader");
    this.source = Validate.notNull(source, "source");
  }

  @Override
  public NodeFile find(String filename) {

    Validate.notNull(filename, "filename");

    Object value = loadedFiles.get(filename);

    if (value == ABSENT) {
      return null;
    } else if (value != null) {
      Validate.state(value instanceof NodeFile,
          "A object that isn't 'UNDEFINED' nor instance of NodeFile is present in" +
              " the loaded files map! Object: '" + value + "'");
      return (NodeFile) value;
    }

    NodeFile nodeFile = null;
    File file = new File(this.source.getFolder(), filename);
    if (file.exists()) {
      try {
        nodeFile = nodeFileLoader.load(source, file);
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, "NMessage can't load file '" +
            filename + "'", e);
      }
    }

    if (nodeFile == null) {
      try (InputStream resource = source.getClassLoader().getResourceAsStream(filename)) {
        if (resource != null) {
          nodeFile = nodeFileLoader.loadAndCreate(source, resource, filename);
        }
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, "NMessage can't load and create" +
            " file from resource '" + filename + "'", e);
      }
    }

    loadedFiles.put(filename, nodeFile == null ? ABSENT : nodeFile);
    return nodeFile;
  }

}
