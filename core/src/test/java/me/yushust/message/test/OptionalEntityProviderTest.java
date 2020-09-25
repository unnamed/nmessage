package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionalEntityProviderTest extends MessageProviderTestCase {

  @Test
  public void test() {

    MessageHandler<ConsoleEntity> handle = MessageHandler.builder(ConsoleEntity.class)
        .setRepository(messageRepository)
        .build();

    Assertions.assertEquals(
        "yea Burgers",
        handle.getMessage("non")
    );

  }

}
