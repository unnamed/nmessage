package me.yushust.message.mode;

import me.yushust.message.specific.Messenger;

/**
 * Represents a message sending mode, for
 * example sending a message as 'NORMAL', 'TITLE_CASE' or
 * 'LOWER_CASE' etc. You can create your own Modes.
 *
 * <p>It's preferable that the implementation is an
 * enum, to avoid creating unnecessary instances</p>
 *
 * <p>With the modes, you can send messages in other
 * form, like in information windows, console, etc by
 * checking the mode in {@link Messenger}.</p>
 *
 * <p>The messageHandler will make sure that Mode
 * is always of the type you previously indicated</p>
 *
 * <pre>
 *   class MyMessenger implements Messenger&#60;MyEntity&#62; {
 *
 *    &#64;Override
 *    public void send(MyEntity entity, Mode mode, String message) {
 *      switch (mode) {
 *       case MyModes.WINDOW: {
 *         WindowManager.showInfo(message);
 *         break;
 *       }
 *       case MyModes.CONSOLE: {
 *         System.out.println(message);
 *         break;
 *       }
 *       // and so on...
 *      }
 *    }
 *
 *   }
 * </pre>
 */
public interface Mode {

  /**
   * Determines if this mode is the
   * default mode. It's used when a
   * Mode isn't specified in the
   * message sending.
   */
  boolean isDefault();

}
