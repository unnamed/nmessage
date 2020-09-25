package me.yushust.message.test;

/**
 * Represents a message receiver, a
 * property holder, a languaged entity,
 * etc.
 */
public class ConsoleEntity {

  private final String language;

  public ConsoleEntity(String language) {
    this.language = language;
  }

  public String getLanguage() {
    return this.language;
  }

  public void send(String log) {
    System.out.println("[Message Received] " + log);
  }

}