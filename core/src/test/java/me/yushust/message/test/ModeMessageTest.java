package me.yushust.message.test;

import me.yushust.message.MessageProvider;
import me.yushust.message.mode.Mode;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModeMessageTest extends HandlerTestCase {

  @Test
  public void test() {

    MessageProvider handler = MessageProvider.create(
        repository,
        wiring -> wiring
            .withModes(Modes.class)
            .specify(Entity.class)
            .setMessenger((entity, mode, message) -> {
              Assertions.assertEquals(Modes.INFO, mode);
              Assertions.assertEquals("I don't know", message);
            })
    );

    handler.send(null, Modes.INFO, "message-get-test");
    handler.send(null, "message-get-test");

  }

  enum Modes implements Mode {
    WARNING, // yellow
    INFO {
      @Override
      public boolean isDefault() {
        return true;
      }
    }; // green

    @Override
    public boolean isDefault() {
      return false;
    }
  }

}
