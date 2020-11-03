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

  public String replace(String string) {
    for (Entry entry : entries) {
      string = string.replace(
          entry.getOldValue(),
          entry.getNewValue()
      );
    }
    return string;
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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Replace(");
    builder.append(entries.size());
    builder.append(") {");
    Iterator<Entry> entryIterator = entries.iterator();
    while (entryIterator.hasNext()) {
      builder.append(entryIterator.next());
      if (entryIterator.hasNext()) {
        builder.append(", ");
      }
    }
    builder.append('}');
    return builder.toString();
  }

  public static ReplacePack make(Object... replacements) {
    Validate.argument(replacements.length % 2 == 0, "The replacement count must be pair! It's " +
        "a tuple of (String, Object)");
    ReplacePack replacePack = new ReplacePack();
    for (int i = 0; i < replacements.length; i += 2) {
      Object key = replacements[i];
      Validate.argument(key instanceof String, "Replaced keys (arguments in 0, " +
          "2, 4, etc) must be strings. But it's '" + key + "' in index: " + i);
      Object value = replacements[i + 1];
      Validate.notNull(value, "value cannot be null. Index: " + (i + 1));
      replacePack.add(key.toString(), value.toString());
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
