package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Test;

public class CyclicMessagesTest extends HandlerTestCase {

  @Test
  public void test() {

    MessageHandler<Entity> handler = MessageHandler.builder(Entity.class)
        .setRepository(repository)
        .build();

    System.out.println(handler.getMessage("linked-message-1"));

  }

}
