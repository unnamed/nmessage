package me.yushust.message.exception;

import me.yushust.message.track.TrackingContext;

public class MessageHandlingException
    extends RuntimeException {

  private static final String STACK_ELEMENT_SEPARATOR = "\n        While getting: ";

  public MessageHandlingException(String message) {
    super(message);
  }

  public MessageHandlingException(
      String message, TrackingContext context
  ) {
    super(formatMessage(message, context));
  }

  public MessageHandlingException(
      String message,
      TrackingContext context,
      Throwable cause
  ) {
    super(formatMessage(message, context), cause);
  }

  private static String formatMessage(
      String message, TrackingContext context
  ) {
    message += "\n    With entity: '" + context.getEntity() + "'";
    message += "\n    In language: '" + context.getLanguage() + "'";

    message += STACK_ELEMENT_SEPARATOR;
    message += context.getPathList().pathsToString(STACK_ELEMENT_SEPARATOR);
    return message;
  }

}
