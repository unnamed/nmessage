package me.yushust.message.file;

/**
 * Represents a loaded path/node-based file
 * (configurations that used paths like
 * "path.other-path.etc" to locate something)
 * Can be YAML, JSON, PROPERTIES files, etc.
 */
public interface NodeFile {

  /** Must return a string or a string list, otherwise the object is converted to string.*/
  Object get(String node);

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
