package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceholderProviderTest extends MessageProviderTestCase {

  @Test
  public void test() {

    MessageHandler<ConsoleEntity> provider = MessageHandler.builder(ConsoleEntity.class)
        .setRepository(messageRepository)
        .setLanguageProvider(ConsoleEntity::getLanguage)
        .addProvider(new HashCodePlaceholderProvider())
        .build();


    ConsoleEntity entity = new ConsoleEntity("en");
    Assertions.assertEquals("Heey, this is your hashcode " + entity.hashCode(),
        provider.getMessage(entity, "hashcode"));

    entity = new ConsoleEntity("es");
    Assertions.assertEquals("Heey, aqui esta tu hashcode " + entity.hashCode(),
        provider.getMessage(entity, "hashcode"));

  }

}
