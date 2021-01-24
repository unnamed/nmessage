package me.yushust.message.track;

import me.yushust.message.MessageProvider;
import me.yushust.message.ReplacePack;
import me.yushust.message.exception.MessageHandlingException;
import me.yushust.message.exception.TrackedException;

/** Context to detect cyclic-linked messages */
public final class TrackingContext {

  /** The entity used to obtain messages and replace variables */
  private final Object entity;
  /** The language used to obtain the messages */
  private final String language;

  /** The just-in-time entities */
  private final Object[] jitEntities;

  /** Pack that contains all the literal replacements for the messages */
  private final ReplacePack literalReplacements;

  /** Pack that contains all the variable replacements for the messages */
  private final ReplacePack variableReplacements;

  /** This is the real path stack, used to detect cyclic linked messages */
  private final TrackingPathList paths;

  private final MessageProvider provider;
  private final ContextRepository contextRepository;

  private TrackingContext(
    Object entity,
    String language,
    Object[] jitEntities,
    ReplacePack literalReplacements,
    ReplacePack variableReplacements,
    TrackingPathList paths,
    MessageProvider provider
  ) {
    this.entity = entity;
    this.language = language;
    this.jitEntities = jitEntities;
    this.literalReplacements = literalReplacements;
    this.variableReplacements = variableReplacements;
    this.paths = paths;
    this.provider = provider;
    this.contextRepository = new ContextRepository(this, provider);
  }

  public TrackingContext(
    Object entity,
    String language,
    Object[] jitEntities,
    ReplacePack literalReplacements,
    ReplacePack variableReplacements,
    MessageProvider provider
  ) {
    this(
      entity,
      language,
      jitEntities,
      literalReplacements,
      variableReplacements,
      TrackingPathList.create(),
      provider
    );
  }

  public TrackingPathList getPathList() {
    return paths;
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

  public void push(String path) {
    try {
      paths.push(path);
    } catch (TrackedException e) {
      throw new MessageHandlingException(
        "Cannot push path to stack", this, e
      );
    }
  }

  public String pop() {
    return paths.pop();
  }

  public TrackingContext with(String language) {
    return new TrackingContext(
      null,
      language,
      jitEntities,
      literalReplacements,
      variableReplacements,
      // use the same path stack because they back
      // to its original state when getMessage execution ends
      paths,
      provider
    );
  }

  @Override
  public String toString() {
    return "TrackingContext " + "(paths=" +
      paths.size() +
      ", hasEntity=" +
      (entity != null) +
      ") {" +
      paths.pathsToString(" <- ") +
      '}';
  }

}
