package me.yushust.message;

/**
 * Represents a box, a placeholder container, for example:
 * - "Hey (placeholder), I love you" -> '(' is START, and ')' is END
 * - "Hey %placeholder%, please love me" -> '%' is START and END
 * - "Hey {placeholder}, listen to me" -> '{' is START and '}' is END
 */
public final class PlaceholderBox {

  public static final PlaceholderBox DEFAULT = new PlaceholderBox('%', '%');

  private final char start;
  private final char end;

  public PlaceholderBox(char start, char end) {
    this.start = start;
    this.end = end;
  }

  public char getStart() {
    return start;
  }

  public char getEnd() {
    return end;
  }

}