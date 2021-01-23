package me.yushust.message.internal;

import me.yushust.message.track.ContextRepository;

import java.io.Closeable;
import java.util.*;

/** Context to detect cyclic-linked messages */
public final class InternalContext
    implements Closeable {

  /** The entity used to obtain messages and replace variables*/
  private final Object entity;
  /** The language used to obtain the messages */
  private final String language;

  /** This is the real path stack, used to detect cyclic linked messages */
  private final LinkedList<String> pathDeque;

  private final MessageHandlerImpl handle;
  private final ContextRepository contextRepository;

  /**
   * The path stack (not really a stack) used to
   * execute contains(...) method in constant time
   */
  private final Set<String> pathSet;

  private InternalContext(
      Object entity,
      String language,
      LinkedList<String> pathDeque,
      Set<String> pathSet,
      MessageHandlerImpl handle
  ) {
    this.entity = entity;
    this.language = language;
    this.pathDeque = pathDeque;
    this.pathSet = pathSet;
    this.handle = handle;
    this.contextRepository = new ContextRepository(this, handle);
  }

  InternalContext(Object entity, String language, MessageHandlerImpl handle) {
    this(
        entity,
        language,
        new LinkedList<>(),
        new HashSet<>(),
        handle
    );
  }

  public ContextRepository getContextRepository() {
    return contextRepository;
  }

  public Object getEntity() {
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

  void popAndCheckSame(String expected) {
    // Illegal state, the path stack is now invalid!
    String obtained = pop();
    if (!expected.equals(obtained)) {
      throw new IllegalStateException("Invalid path stack, the obtained path isn't "
          + "equals to the previously pushed path!\n    Expected: " + expected
          + "\n    Obtained: " + obtained);
    }
  }

  String pop() {
    String popped = pathDeque.removeFirst();
    pathSet.remove(popped);
    return popped;
  }

  public InternalContext with(String language) {
    return new InternalContext(
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

  @Override
  public void close() {

  }

}
