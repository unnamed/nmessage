package me.yushust.message.core.intercept;

/**
 *
 */
public final class Loop {

    private Loop() {
        throw new UnsupportedOperationException("This class couldn't be instantiated!");
    }

    public static class Pointer {

        private int value;

        public int get() {
            return value;
        }

        public void set(int value) {
            this.value = value;
        }

        public int incrementAndGet() {
            return ++value;
        }

        public int getAndIncrement() {
            return value++;
        }

    }

    public enum End {
        BREAK,
        CONTINUE,
        NORMAL
    }
}
