package me.yushust.message.internal;

import java.util.*;

/**
 * Context to detect cyclic-linked messages,
 * etc. The relation of contexts with the message
 * handler is "A Context per Thread per MessageHandler"
 *
 * <p>The message handler normally contains a
 * ThreadLocal for the contexts</p>
 *
 * @param <E> Represents the entity
 */
// Implements MessageHandler to ease its use in the providers
public final class FormattingContext<E> {

  /** The entity used to obtain messages and replace variables*/
  private final E entity;
  /** The language used to obtain the messages */
  private final String language;

  /** This is the real path stack, used to detect cyclic linked messages */
  private final LinkedList<String> pathDeque =
      new LinkedList<>();

  /**
   * The path stack (not really a stack) used to
   * execute contains(...) method in constant time
   */
  private final Set<String> pathSet =
      new HashSet<>();

  FormattingContext(E entity, String language) {
    this.entity = entity;
    this.language = language;
  }

  public E getEntity() {
    return entity;
  }

  /** The language used to obtain the messages */
  public String getLanguage() {
    return language;
  }

  public boolean isEmpty() {
    return pathSet.isEmpty();
  }

  public boolean has(String path) {
    return pathSet.contains(path);
  }

  void push(String path) {
    pathDeque.addFirst(path);
    pathSet.add(path);
  }

  String pop() {
    String popped = pathDeque.removeFirst();
    pathSet.remove(popped);
    return popped;
  }

  public List<String> export() {
    return Collections.unmodifiableList(pathDeque);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("FormatContext ");
    builder.append("(paths=");
    builder.append(pathSet.size());
    builder.append(", hasEntity=");
    builder.append(entity != null);
    builder.append(") {");
    Iterator<String> pathIterator = pathDeque.iterator();
    while (pathIterator.hasNext()) {
      builder.append(pathIterator.next());
      if (pathIterator.hasNext()) {
        builder.append(" -> ");
      }
    }
    builder.append('}');
    return builder.toString();
  }

}
