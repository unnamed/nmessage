package me.yushust.message.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Wrapper of {@link List} of {@link String}s
 * used to ease its element handling by adding
 * some utility methods
 */
public final class StringList
        extends AbstractList<String>
        implements List<String> {

    /**
     * The wrapped original list
     */
    private final List<String> value;

    /**
     * Constructs a new {@link StringList} from
     * the given {@code value}. The internal list
     * isn't defined to the given {@code value},
     * the stored internal list is a just copy of
     * the given {@code value}
     */
    @Contract("null -> fail")
    public StringList(List<String> value) {
        Validate.isNotNull(value, "value");
        this.value = new ArrayList<>(value);
    }

    /**
     * Creates an immutable list of a single string element,
     * and then it's wrapped with {@link StringList} (mutable)
     *
     * @param element The unique element
     * @see Collections#singletonList
     */
    public static StringList singleton(String element) {
        return new StringList(Collections.singletonList(element));
    }

    /**
     * Executes the {@link String#replace} with the
     * given {@code key} and {@code replacements} as
     * arguments in all the elements, so the internal
     * list mutates.
     *
     * @return The same string list represented by {@code this},
     * for a fluent api
     */
    @Contract("null, _ -> fail")
    public StringList replace(String key, @Nullable Object replacement) {
        Validate.isNotNull(key, "key");
        // convert null literal to "null"
        String replacementStr = String.valueOf(replacement);
        value.replaceAll(line -> line.replace(key, replacementStr));
        return this;
    }

    /**
     * Calls {@link String#join} passing as arguments the
     * given {@code delimiter} and the internal list.
     *
     * @param delimiter The joined elements delimiter
     * @return The joined elements
     */
    @Contract("null -> fail")
    public String join(String delimiter) {
        return String.join(delimiter, value);
    }

    /**
     * Returns a random element from the list, or
     * {@code valueIfEmpty} if the list is empty
     *
     * @deprecated Getting random elements should
     * be made by library users, not by the library
     * itself.
     */
    @Deprecated
    public String random(String valueIfEmpty) {
        if (value.isEmpty()) {
            return valueIfEmpty;
        } else {
            return getRandomElement();
        }
    }

    /**
     * Returns a random element from the list
     *
     * @throws NoSuchElementException if list is empty
     * @deprecated Getting random elements should
     * be made by library users, not by the library
     * itself.
     */
    @Deprecated
    public String random() {
        if (value.isEmpty()) {
            throw new NoSuchElementException("No elements in the list");
        } else {
            return getRandomElement();
        }
    }

    /**
     * Returns a random element from the list
     *
     * @deprecated Getting random elements should
     * be made by library users, not by the library
     * itself.
     */
    @Deprecated
    private String getRandomElement() {
        // random isn't supported
        return value.get(0);
    }

    /**
     * @return Returns the internal string list
     * @deprecated We shouldn't expose the internal
     * list, it's internal!
     */
    @Deprecated
    public List<String> getContents() {
        return value;
    }

    //#region Delegate methods functionality to internal string list
    @Override
    public String get(int index) {
        return value.get(index);
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public String set(int index, String element) {
        return value.set(index, element);
    }

    @Override
    public void add(int index, String element) {
        value.add(index, element);
    }
    //#endregion

    @Override
    public String remove(int index) {
        return value.remove(index);
    }

}
