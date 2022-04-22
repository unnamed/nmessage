package me.yushust.message.exception;

public class CyclicLinkedMessagesException
        extends TrackedException {

    public CyclicLinkedMessagesException() {
    }

    public CyclicLinkedMessagesException(String message) {
        super(message);
    }

    public CyclicLinkedMessagesException(Throwable cause) {
        super(cause);
    }

    public CyclicLinkedMessagesException(String message, Throwable cause) {
        super(message, cause);
    }

}
