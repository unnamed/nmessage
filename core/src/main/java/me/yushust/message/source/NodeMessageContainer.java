package me.yushust.message.source;

/**
 * Represents an in-memory message container
 * for obtaining messages, it can be directly enums,
 * files or something else
 */
public interface NodeMessageContainer {

  /**
   * Must return a string or a string list,
   * otherwise the object is converted to string.
   */
  Object get(String node);

}
