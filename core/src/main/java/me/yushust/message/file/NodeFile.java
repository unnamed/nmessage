package me.yushust.message.file;

import java.util.List;
import java.util.Optional;

/**
 * Represents a loaded path/node-based file
 * (configurations that used paths like
 * "path.other-path.etc" to locate something)
 * Can be YAML, JSON, PROPERTIES files, etc.
 */
public interface NodeFile {

  /** Returns the string at the specified node */
  String getString(String node);

  /** Returns the string list at the specified node */
  List<String> getStringList(String node);

  /**
   * {@inheritDoc}
   * The implementation must return its
   * name and its file extension.
   * For example
   * <pre>
   * &#64;Override
   * public String toString() {
   *   return getClass().getSimpleName() + "(*.properties)";
   * }
   * </pre>
   */
  @Override
  String toString();

}
