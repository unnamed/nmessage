package me.yushust.message.strategy;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Strategy for handle errors and message
 * exceptions, designed to be overwritten.
 */
@SuppressWarnings("unused")
public class Strategy {

  /**
   * Called when something fails, the cause
   * and the original text is passed to the method
   */
  public void onFail(Notify.Failure cause, String text) {
  }

  /**
   * Called when something went wrong but it can be ignored,
   * for example a cyclic linked message. The path stack is
   * provided.
   */
  public void warn(Notify.Warning cause, List<String> pathStack) {
  }

  /**
   * Called when a message isn't found
   */
  @Nullable
  public String getNotFoundMessage(String language, String path) {
    return path;
  }

}
