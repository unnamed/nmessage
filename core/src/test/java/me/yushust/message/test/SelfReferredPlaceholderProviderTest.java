package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageRepository;
import me.yushust.message.placeholder.PlaceholderProvider;
import me.yushust.message.placeholder.annotation.ProviderIdentifier;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SelfReferredPlaceholderProviderTest extends MessageProviderTestCase {

    @Test
    public void test() {

        MessageHandler<ConsoleEntity> provider = MessageHandler.<ConsoleEntity>builder()
                .setRepository(messageRepository)
                .addProvider(new SelfReferredPlaceholderProvider())
                .build();

        ConsoleEntity entity = new ConsoleEntity("en");

        Assertions.assertEquals("yea yea %test_test% yea yea",
                provider.getMessage(entity, "stack_message"));
    }

    @ProviderIdentifier("test")
    public static class SelfReferredPlaceholderProvider extends PlaceholderProvider<ConsoleEntity> {

        @Override
        public @Nullable String replace(MessageRepository repository, ConsoleEntity entity, String parameters) {

            if (parameters.equals("test")) {
                return repository.getMessage("stack_message");
            }

            return null;
        }

    }

}
