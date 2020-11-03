package me.yushust.message.internal;

import me.yushust.message.ContextRepository;

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
public final class InternalContext<E> {

  /** The entity used to obtain messages and replace variables*/
  private final E entity;
  /** The language used to obtain the messages */
  private final String language;

  /** This is the real path stack, used to detect cyclic linked messages */
  private final LinkedList<String> pathDeque;

  private final MessageHandlerImpl<E> handle;
  private final ContextRepository<E> contextRepository;

  /**
   * The path stack (not really a stack) used to
   * execute contains(...) method in constant time
   */
  private final Set<String> pathSet;

  private InternalContext(
      E entity,
      String language,
      LinkedList<String> pathDeque,
      Set<String> pathSet,
      MessageHandlerImpl<E> handle
  ) {
    this.entity = entity;
    this.language = language;
    this.pathDeque = pathDeque;
    this.pathSet = pathSet;
    this.handle = handle;
    this.contextRepository = new ContextRepository<>(this, handle);
  }

  InternalContext(E entity, String language, MessageHandlerImpl<E> handle) {
    this(
        entity,
        language,
        new LinkedList<>(),
        new HashSet<>(),
        handle
    );
  }

  ContextRepository<E> getContextRepository() {
    return contextRepository;
  }

  public E getEntity() {
    return entity;
  }

  /** The language used to obtain the messages */
  public String getLanguage() {
    return language;
  }

  boolean has(String path) {
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

  public InternalContext<E> with(String language) {
    return new InternalContext<>(
        null,
        language,
        // use the same path stack and set
        //because they back to its original
        //state when getMessage execution ends
        pathDeque,
        pathSet,
        handle
    );
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
