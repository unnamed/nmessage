package me.yushust.message.allocate;

import me.yushust.message.source.LoadSource;
import me.yushust.message.source.NodeFile;
import me.yushust.message.source.NodeFileLoader;

/**
 * Uses {@link LoadSource} and {@link NodeFileLoader} to
 * load {@link NodeFile}s
 */
public interface NodeFilePool {

  /**
   * Finds a {@link NodeFile} with the specified
   * {@code filename}. It can load the file from
   * the resources or from the specified {@link LoadSource}
   */
  NodeFile find(String filename);

  /**
   * Creates an instance of the default implementation of
   * NodeFilePool using the given loader and source
   */
  static NodeFilePool createDefault(NodeFileLoader nodeFileLoader, LoadSource loadSource) {
    return new NodeFilePoolImpl(nodeFileLoader, loadSource);
  }

}
