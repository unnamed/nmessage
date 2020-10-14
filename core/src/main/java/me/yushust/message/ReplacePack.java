package me.yushust.message;

import me.yushust.message.util.Validate;

import java.util.*;

public final class ReplacePack implements Cloneable {

  public static final ReplacePack EMPTY = new ReplacePack(Collections.emptyList());

  private final List<Entry> entries;

  private ReplacePack(List<Entry> entries) {
    this.entries = entries;
  }

  public ReplacePack() {
    this(new ArrayList<>());
  }

  public ReplacePack add(String oldValue, String newValue) {
    entries.add(new Entry(oldValue, newValue));
    return this;
  }

  public List<Entry> getEntries() {
    return entries;
  }

  @Override
  public ReplacePack clone() {
    if (this == EMPTY) {
      return this;
    }
    return new ReplacePack(new ArrayList<>(entries));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReplacePack that = (ReplacePack) o;
    return Objects.equals(entries, that.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries);
  }

  public static ReplacePack make(String... replacements) {
    Validate.argument(replacements.length % 2 == 0, "The replacement count must be pair! It's " +
        "a tuple of (String, String)");
    ReplacePack replacePack = new ReplacePack();
    for (int i = 0; i < replacements.length; i += 2) {
      String key = replacements[i];
      String value = replacements[i + 1];
      replacePack.add(key, value);
    }
    return replacePack;
  }

  public static final class Entry {

    private final String oldValue;
    private final String newValue;

    private Entry(String oldValue, String newValue) {
      this.oldValue = Validate.notNull(oldValue, "oldValue");
      this.newValue = Validate.notNull(newValue, "newValue");
    }

    public String getOldValue() {
      return oldValue;
    }

    public String getNewValue() {
      return newValue;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Entry that = (Entry) o;
      return Objects.equals(oldValue, that.oldValue) &&
          Objects.equals(newValue, that.newValue);
    }

    @Override
    public int hashCode() {
      return Objects.hash(oldValue, newValue);
    }

    @Override
    public String toString() {
      return "'" + oldValue + "': '" + newValue + "'";
    }
  }
}
