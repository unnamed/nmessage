package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContextualMessageGetTest extends HandlerTestCase {

  @Test
  public void test() {

    MessageHandler handler = MessageHandler.create(
        repository,
        wiring -> wiring.specify(Entity.class)
            .addProvider("obj", testProvider())
    );

    Entity entity = new Entity();
    Assertions.assertEquals(
        "Check my github https://github.com/yusshu 'i'manentity'",
        handler.get(entity, "ctx-message")
    );
  }

}
