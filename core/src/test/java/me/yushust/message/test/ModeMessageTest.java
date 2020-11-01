package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.mode.Mode;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModeMessageTest extends HandlerTestCase {

  @Test
  public void test() {

    MessageHandler<Entity> handler = MessageHandler.builder(Entity.class)
        .setRepository(repository)
        .setModes(Modes.class, Modes.values())
        .setMessenger((entity, mode, message) -> {
          Assertions.assertEquals(Modes.INFO, mode);
          Assertions.assertEquals("I don't know", message);
        })
        .build();

    handler.send(null, Modes.INFO, "message-get-test");
    handler.send(null,"message-get-test");

  }

  enum Modes implements Mode {
    WARNING, // yellow
    INFO {
      @Override
      public boolean isDefault() {
        return true;
      }
    } // green
    ;

    @Override
    public boolean isDefault() {
      return false;
    }
  }

}
