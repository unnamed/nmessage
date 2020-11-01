package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageGetTest extends HandlerTestCase {

  @Test
  public void test() {
    MessageHandler<Entity> messageHandler = MessageHandler.builder(Entity.class)
        .setRepository(repository)
        .build();

    Assertions.assertEquals("I don't know", messageHandler.getMessage("message-get-test"));
  }

}