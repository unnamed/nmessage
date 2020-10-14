package me.yushust.message.internal;

import me.yushust.message.util.Validate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Context to detect cyclic-linked messages,
 * etc. The relation of contexts with the message
 * handler is "An InternalContext per Thread per MessageHandler"
 *
 * <p>The message handler normally contains a
 * ThreadLocal for the contexts</p>
 *
 * @param <E> Represents the entity
 */
class InternalContext<E> {

  /** The entity to obtain messages in its languag*/
  private final E entity;

  /** This is the real path stack, used to detect cyclic linked messages */
  private final LinkedList<String> pathDeque =
      new LinkedList<>();

  InternalContext(E entity) {
    this.entity = entity;
  }

  E getEntity() {
    return entity;
  }

  boolean has(String path) {
    // TODO: Use a Set instead of a LinkedList
    return pathDeque.contains(path);
  }

  void push(String path) {
    Validate.notNull(path, "path");
    pathDeque.addFirst(path);
  }

  String pop() {
    return pathDeque.removeFirst();
  }

  List<String> export() {
    return Collections.unmodifiableList(pathDeque);
  }

}
