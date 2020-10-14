package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;
import me.yushust.message.PlaceholderProvider;
import me.yushust.message.ProviderIdentifier;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SelfReferredPlaceholderProviderTest extends MessageProviderTestCase {

  @Test
  public void test() {

    MessageHandler<ConsoleEntity> provider = MessageHandler.builder(ConsoleEntity.class)
        .setRepository(messageRepository)
        .addProvider(new SelfReferredPlaceholderProvider())
        .build();

    ConsoleEntity entity = new ConsoleEntity("en");

    Assertions.assertEquals("yea yea %test_test% yea yea",
        provider.getMessage(entity, "stack_message"));
  }

  @ProviderIdentifier("test")
  public static class SelfReferredPlaceholderProvider implements PlaceholderProvider<ConsoleEntity> {

    @Override
    public @Nullable String replace(MessageRepository repository, ConsoleEntity entity, String parameters) {

      if (parameters.equals("test")) {
        return repository.getMessage("stack_message");
      }

      return null;
    }

  }

}
