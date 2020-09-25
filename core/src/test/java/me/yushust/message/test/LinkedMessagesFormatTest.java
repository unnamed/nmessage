package me.yushust.message.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.yushust.message.MessageHandler;

public class LinkedMessagesFormatTest extends MessageProviderTestCase {

  @Test
  public void test() {

    MessageHandler<ConsoleEntity> provider = MessageHandler.builder(ConsoleEntity.class)
        .setRepository(messageRepository)
        .setLanguageProvider(ConsoleEntity::getLanguage)
        .addProvider(new HashCodePlaceholderProvider())
        .build();

    ConsoleEntity entity = new ConsoleEntity("en");
    Assertions.assertEquals("The message \"Heey, this is your hashcode " +
        entity.hashCode() + "\" isn't useful", provider.getMessage(entity, "that"));

  }

}