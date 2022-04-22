package me.yushust.message.exception;

import me.yushust.message.track.TrackingContext;

public class TrackedException
        extends RuntimeException {

    public TrackedException() {
    }

    public TrackedException(String message) {
        super(message);
    }

    public TrackedException(Throwable cause) {
        super(cause);
    }

    public TrackedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageHandlingException withContext(TrackingContext context) {
        return new MessageHandlingException(getMessage(), context, this);
    }

}
