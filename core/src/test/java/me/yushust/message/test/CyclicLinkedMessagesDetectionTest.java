package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CyclicLinkedMessagesDetectionTest extends MessageProviderTestCase {

  @Test
  public void test() {

    MessageHandler<ConsoleEntity> provider = MessageHandler.builder(ConsoleEntity.class)
        .setRepository(messageRepository)
        .setLanguageProvider(ConsoleEntity::getLanguage)
        .build();

    ConsoleEntity entity = new ConsoleEntity("en");
    Assertions.assertEquals("Foo bar Foo bar %path_baz% foo yes foo", provider.getMessage(entity, "bruh"));
    Assertions.assertEquals("Fuah bruh Bro bro bro Burgers yea", provider.getMessage(entity, "fooo"));

  }

}
