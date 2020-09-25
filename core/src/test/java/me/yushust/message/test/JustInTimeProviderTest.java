package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.placeholder.annotation.ProviderIdentifier;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JustInTimeProviderTest extends MessageProviderTestCase {

  @ProviderIdentifier("string")
  public static class StringPlaceholderProvider implements PlaceholderProvider<String> {

    @Override
    public @Nullable String replace(MessageRepository repository, String entity, String parameters) {
      switch (parameters.toLowerCase()) {
        case "x2": {
          return entity + entity;
        }
        case "x3": {
          return entity + entity + entity;
        }
        default: return null;
      }
    }
  }

  @Test
  public void test() {

    MessageHandler<ConsoleEntity> handler = MessageHandler.builder(ConsoleEntity.class)
        .setRepository(messageRepository)
        .addExternalProvider(String.class, new StringPlaceholderProvider())
        .build();

    ConsoleEntity entity = new ConsoleEntity("en");

    Assertions.assertEquals("2 is naninani and 3 naninaninani",
        handler.getMessage(entity, "ye", "nani"));

  }

}
