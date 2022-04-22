package me.yushust.message.util;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents a class that holds a
 * list of string replacements.
 *
 * <p>A simple struct similar to a
 * (String, Object)[] (not allowed in java)</p>
 */
public final class ReplacePack
        implements Cloneable {

    /**
     * Constant replacement pack that doesn't
     * hold replacement elements
     */
    public static final ReplacePack EMPTY
            = new ReplacePack(Collections.emptyList());

    /**
     * Internal list that contains all the replacements
     * in insertion order
     *
     * @see ReplacePack#add(String, String)
     */
    private final List<Entry> entries;

    /**
     * Constructs a new instance with the given initial
     * {@code entries}. Internal usage only
     *
     * @param entries The initial entries
     */
    private ReplacePack(List<Entry> entries) {
        this.entries = entries;
    }

    /**
     * Constructs a new instance with no initial
     * replacements. Calls {@link ReplacePack#ReplacePack(List)}
     * passing a new {@link ArrayList} as parameter
     */
    public ReplacePack() {
        this(new ArrayList<>());
    }

    /**
     * Creates a new {@link ReplacePack} from a {@link Object}
     * var-args array by converting the elements at even indexes
     * to strings (representing replaced strings) and elements
     * at odd indexes representing replacement string values
     *
     * <p>If no arguments were provided, {@link ReplacePack#EMPTY}
     * is returned</p>
     */
    public static ReplacePack make(Object... replacements) {

        Validate.isTrue(
                replacements.length % 2 == 0,
                "The given replacements array isn't pair!"
        );

        // return the constant empty replacement pack on no replacements
        if (replacements.length == 0) {
            return ReplacePack.EMPTY;
        }

        ReplacePack replacePack = new ReplacePack();

        for (int i = 0; i < replacements.length; i++) {
            Object key = replacements[i];
            Validate.isTrue(
                    key instanceof String,
                    "Elements in event indexes must be strings. Unexpected '" + key + "' at index: " + i
            );
            Object value = replacements[++i];
            Validate.isNotNull(value, "value cannot be null. Index: " + i);
            replacePack.add(key.toString(), value.toString());
        }

        return replacePack;
    }

    /**
     * Constructs and adds a new entry to the
     * internal replacements list. Fluent method
     *
     * @param oldValue The replaced string value
     * @param newValue The replacement string value
     * @return The {@link ReplacePack} represented
     * by {@code this}. Fluent API
     */
    public ReplacePack add(String oldValue, String newValue) {
        entries.add(new Entry(oldValue, newValue));
        return this;
    }

    /**
     * Returns the internal list of this replacement pack
     *
     * @deprecated The replacement pack may change its internal
     * structure, so it should not be exposed.
     */
    @Deprecated
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * Applies the replacement changes to the given
     * {@code list} string elements
     *
     * @param list List containing all the target strings
     * @return The given {@code list}
     */
    @Contract("null -> fail; _ -> param1")
    public StringList replace(StringList list) {
        Validate.isNotNull(list, "list");
        for (Entry entry : entries) {
            list.replace(
                    entry.getOldValue(),
                    entry.getNewValue()
            );
        }
        return list;
    }

    /**
     * Applies the replacement changes to the
     * given {@code string} target
     *
     * @param string The target string
     * @return The modified string, because strings
     * are immutable
     */
    @Contract("null -> fail")
    public String replace(String string) {
        Validate.isNotNull(string, "string");
        for (Entry entry : entries) {
            string = string.replace(
                    entry.getOldValue(),
                    entry.getNewValue()
            );
        }
        return string;
    }

    /**
     * Clones the {@link ReplacePack} represented
     * by {@code this}. This method doesn't call
     * the native {@link Object#clone()}, it just
     * creates a new instance of {@link ReplacePack}
     * including all the elements in this replacement
     * pack.
     *
     * <p>A {@link ReplacePack} isn't instantiated if
     * the current instance is {@link ReplacePack#EMPTY}</p>
     */
    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public ReplacePack clone() {
        if (this == EMPTY) {
            return this;
        } else {
            return new ReplacePack(new ArrayList<>(entries));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReplacePack that = (ReplacePack) o;
        return entries.equals(that.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    /**
     * Converts the {@link ReplacePack} represented
     * by {@code this} by a human-readable json-like
     * string with a pretty format like "Replace(size)
     * {entries separated by comma}", i.e. "Replace(1)
     * {'x': 'y'}"
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Replace(")
                .append(entries.size())
                .append(") {");
        Iterator<Entry> entryIterator = entries.iterator();
        while (entryIterator.hasNext()) {
            builder.append(entryIterator.next());
            if (entryIterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.append('}').toString();
    }

    /**
     * Represents a {@link ReplacePack} replacement.
     * Holds an old value and a new value. Immutable
     */
    public static final class Entry {

        /**
         * The replaced string value (old)
         */
        private final String oldValue;

        /**
         * The replacement string value (new)
         */
        private final String newValue;

        /**
         * Constructs a new instance from the
         * given {@code oldValue} and {@code newValue}.
         * Null values aren't supported.
         */
        private Entry(String oldValue, String newValue) {
            this.oldValue = Validate.isNotNull(oldValue, "oldValue");
            this.newValue = Validate.isNotNull(newValue, "newValue");
        }

        /**
         * Returns the replaced string value
         */
        public String getOldValue() {
            return oldValue;
        }

        /**
         * Returns the replacement string value
         */
        public String getNewValue() {
            return newValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry that = (Entry) o;
            return oldValue.equals(that.oldValue)
                    && newValue.equals(that.newValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(oldValue, newValue);
        }

        /**
         * Converts the {@link ReplacePack} represented
         * by {@code this} to a human-readable json-like
         * entry format. i.e. "'key': 'value'"
         */
        @Override
        public String toString() {
            return "'" + oldValue + "': '" + newValue + "'";
        }

    }

}
