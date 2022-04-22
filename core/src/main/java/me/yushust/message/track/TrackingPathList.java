package me.yushust.message.track;

import me.yushust.message.exception.CyclicLinkedMessagesException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a list of paths, used to maintain
 * a control across linked messages and detect
 * infinite cycles. This data-structure is compound
 * by a simple hash table (with no values, with not
 * null strings as keys) and a simple LIFO (Last In,
 * First Out) linked list.
 *
 * <p>This class is used instead of a {@link java.util.HashMap}
 * for simple micro-optimization purposes because the path
 * tracking is recurrently required</p>
 *
 * <p>Hash-table part is based (specially the resize function)
 * on the Java {@link java.util.HashMap}</p>
 *
 * <strong>Note that this class isn't thread-safe and
 * doesn't prevent concurrent modifications</strong>
 */
public class TrackingPathList implements Iterable<String> {

    /**
     * The maximum capacity for the hash table
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Array of all the buckets of the paths hash table
     */
    private Entry[] entries;

    /**
     * The threshold of the paths hash table
     */
    private int threshold;

    /**
     * The current structure size
     */
    private int size;

    /**
     * Head of the paths linked list
     */
    private Node head;

    /**
     * Constructs a new tracking path list with the specified initial capacity
     */
    private TrackingPathList(int initialCapacity) {
        this.threshold = getThresholdForCapacity(initialCapacity);
    }

    private TrackingPathList() {
    }

    public static TrackingPathList newWithInitialCapacity(int initialCapacity) {
        return new TrackingPathList(initialCapacity);
    }

    public static TrackingPathList create() {
        return new TrackingPathList();
    }

    private static int getThresholdForCapacity(int capacity) {
        capacity--;
        capacity |= capacity >>> 1;
        capacity |= capacity >>> 2;
        capacity |= capacity >>> 4;
        capacity |= capacity >>> 8;
        capacity |= capacity >>> 16;
        if (capacity < 0) {
            return 1;
        } else if (capacity >= MAXIMUM_CAPACITY) {
            return MAXIMUM_CAPACITY;
        } else {
            return capacity + 1;
        }
    }

    /**
     * @return The current structure size
     */
    public int size() {
        return size;
    }

    /**
     * @return True if the structure is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Gets and removes the last inserted value
     */
    public String pop() {
        if (head == null) {
            throw new NoSuchElementException("No more elements");
        } else {
            String value = head.value;
            head = head.next;
            removeEntry(value);
            return value;
        }
    }

    /**
     * Pushes the given {@code value}
     *
     * @throws NullPointerException     If value is null
     * @throws IllegalArgumentException If value is already contained here
     */
    @Contract("null -> fail")
    public void push(String value) {
        if (value == null) {
            throw new NullPointerException("Elements are not null");
        } else if (addEntry(value)) {
            throw new CyclicLinkedMessagesException(
                    "Already present path: '" + value + '\''
            );
        } else {
            head = new Node(head, value);
        }
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new IteratorImpl(head);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("TrackingPathList (");
        builder.append(size).append(") [");
        pathsToString(builder, " <- ");
        builder.append(']');
        return builder.toString();
    }

    public String pathsToString(String separator) {
        return pathsToString(new StringBuilder(), separator);
    }

    private String pathsToString(
            StringBuilder builder,
            String separator
    ) {
        Node iterating = head;
        while (true) {
            builder.append(iterating.value);
            iterating = iterating.next;
            if (iterating == null) {
                break;
            } else {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    /**
     * Adds the specified {@code key} to the hash table
     *
     * @param key The removed key
     * @return True if the key was already present
     */
    private boolean addEntry(String key) {

        // initial resize when table is null or empty
        if (entries == null || entries.length == 0) {
            entries = resize();
        }

        int hash = key.hashCode();
        int index = (entries.length - 1) & hash;

        Entry entry = entries[index];
        boolean contained = false;

        if (entry == null) {
            entries[index] = new Entry(hash, key, null);
        } else if (key.equals(entry.key)) {
            contained = true;
        } else {
            while (true) {
                // added
                if (entry.next == null) {
                    entry.next = new Entry(hash, key, null);
                    break;
                }
                // already set here
                if (key.equals(entry.key)) {
                    contained = true;
                    break;
                }
                entry = entry.next;
            }
        }

        if (!contained && ++size > threshold) {
            resize();
        }

        return contained;
    }

    /**
     * Removes the entry with the key {@code key}
     * from the hash table
     *
     * @param key The removed key
     * @return True if the key was removed from here
     */
    private boolean removeEntry(String key) {

        if (entries == null || entries.length < 1) {
            return false;
        }

        int index = (entries.length - 1) & key.hashCode();
        Entry checking = entries[index];
        Entry previous = null;

        while (checking != null) {
            if (key.equals(checking.key)) {
                if (previous == null) {
                    // 'entry' wasn't re-assigned, so it's the first node
                    // and we need to change the value in the table
                    entries[index] = checking.next;
                } else {
                    // we set the next node of the previous node to the next
                    // node of this node, removing the reference for the
                    // current node
                    previous.next = checking.next;
                }
                size--;
                return true;
            }
            // it now contains the previous node for the next loop
            previous = checking;
            checking = checking.next;
        }
        return false;
    }

    private Entry[] resize() {

        Entry[] oldTab = entries;
        int oldCapacity = (oldTab == null) ? 0 : oldTab.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold = 0;

        if (oldCapacity > 0) {
            if (oldCapacity >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return entries;
            } else {
                newCapacity = oldCapacity << 1;
                if (
                        newCapacity < MAXIMUM_CAPACITY
                                && oldCapacity >= 16
                ) {
                    newThreshold = oldThreshold << 1;
                }
            }
        } else if (oldThreshold > 0) {
            newCapacity = oldThreshold;
        } else {
            newCapacity = 16;
            newThreshold = 12;
        }

        if (newThreshold == 0) {
            float capacity = (float) newCapacity * 0.75F;
            boolean replaceThreshold = newCapacity < MAXIMUM_CAPACITY
                    && capacity < MAXIMUM_CAPACITY;
            if (replaceThreshold) {
                newThreshold = (int) capacity;
            } else {
                newThreshold = Integer.MAX_VALUE;
            }
        }

        threshold = newThreshold;
        Entry[] newTab = new Entry[newCapacity];
        entries = newTab;

        if (oldTab == null) {
            return newTab;
        }
        for (int j = 0; j < oldCapacity; ++j) {
            Entry entry = oldTab[j];
            if (entry == null) {
                continue;
            }
            oldTab[j] = null;
            if (entry.next == null) {
                int index = entry.hash & (newCapacity - 1);
                newTab[index] = entry;
                continue;
            }
            Entry loHead = null,
                    loTail = null,
                    hiHead = null,
                    hiTail = null;
            Entry next;
            do {
                next = entry.next;
                if ((entry.hash & oldCapacity) == 0) {
                    if (loTail == null) {
                        loHead = entry;
                    } else {
                        loTail.next = entry;
                    }
                    loTail = entry;
                } else {
                    if (hiTail == null) {
                        hiHead = entry;
                    } else {
                        hiTail.next = entry;
                    }
                    hiTail = entry;
                }
            } while ((entry = next) != null);

            if (loTail != null) {
                loTail.next = null;
                newTab[j] = loHead;
            }
            if (hiTail != null) {
                hiTail.next = null;
                newTab[j + oldCapacity] = hiHead;
            }
        }
        return newTab;
    }

    /**
     * Iterator that iterates over the simple linked list
     */
    static class IteratorImpl implements Iterator<String> {

        private Node current;

        IteratorImpl(Node head) {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public String next() {
            String value = current.value;
            current = current.next;
            return value;
        }

    }

    /**
     * Represents an entry for the hash table of the tracked paths
     */
    static class Entry {

        final int hash;
        final String key;
        Entry next;

        Entry(int hash, String key, Entry next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }

    }

    /**
     * Represents a node for the linked list of the tracked paths
     */
    static class Node {

        final Node next;
        final String value;

        Node(Node next, String value) {
            this.next = next;
            this.value = value;
        }

    }

}
