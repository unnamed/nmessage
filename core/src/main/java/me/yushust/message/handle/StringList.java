package me.yushust.message.handle;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

/**
 * A wrapper for a List of Strings, adds methods for
 * facilitate handling
 */
public final class StringList extends AbstractList<String> {

  private final List<String> stringList;

  public StringList(List<String> stringList) {
    this.stringList = stringList;
  }

  /**
   * Uses {@link String#replace} in all elements
   *
   * @param key         The old sequence
   * @param replacement The new sequence
   * @return The string list, for a fluent api
   */
  public StringList replace(String key, Object replacement) {
    if (key != null) {
      stringList.replaceAll(line ->
          line.replace(key, String.valueOf(replacement))
      );
    }
    return this;
  }

  /**
   * Calls {@link String#join} and returns the result
   *
   * @param delimiter The joined elements delimiter
   * @return The joined elements
   */
  public String join(String delimiter) {
    return String.join(delimiter, stringList);
  }

  /**
   * @return Returns the original string list
   */
  public List<String> getContents() {
    return stringList;
  }

  // delegate methods to original string list
  @Override
  public String get(int index) {
    return stringList.get(index);
  }

  @Override
  public int size() {
    return stringList.size();
  }

  @Override
  public String set(int index, String element) {
    return stringList.set(index, element);
  }

  @Override
  public void add(int index, String element) {
    stringList.add(index, element);
  }

  @Override
  public String remove(int index) {
    return stringList.remove(index);
  }

  /**
   * Creates an immutable list of just a string, wrapped
   * with {@link StringList}
   *
   * @param element The unique element
   * @return The string list
   */
  public static StringList singleton(String element) {
    return new StringList(Collections.singletonList(element));
  }

}
