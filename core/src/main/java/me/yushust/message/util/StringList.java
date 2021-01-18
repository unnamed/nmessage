package me.yushust.message.util;

import java.util.*;

/**
 * A wrapper for a List of Strings, adds methods for
 * facilitate handling
 */
public final class StringList extends AbstractList<String> {

  /** Random number generator used to get a random element from the list*/
  private static final Random RANDOM = new Random();

  private final List<String> stringList;

  public StringList(List<String> stringList) {
    this.stringList = new ArrayList<>(stringList);
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
   * @return Random element from the list, or
   * {@code valueIfEmpty} if the list is empty
   */
  public String random(String valueIfEmpty) {
    if (stringList.isEmpty()) {
      return valueIfEmpty;
    } else {
      return getRandomElement();
    }
  }

  /**
   * @return Random element from the list
   * @throws NoSuchElementException if list is empty
   */
  public String random() {
    if (stringList.isEmpty()) {
      throw new NoSuchElementException("No elements in the list");
    } else {
      return getRandomElement();
    }
  }

  /** @return Random element from the list */
  private String getRandomElement() {
    return stringList.get(
        RANDOM.nextInt(stringList.size())
    );
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
