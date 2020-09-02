package me.yushust.message.test;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.placeholder.PlaceholderProvider;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SelfReferredPlaceholderProviderTest extends MessageProviderTestCase {

    @Test
    public void test() {

        MessageProvider<ConsoleEntity> provider = MessageProvider.<ConsoleEntity>builder()
                .setRepository(messageRepository)
                .addProvider(new SelfReferredPlaceholderProvider())
                .build();

        ConsoleEntity entity = new ConsoleEntity("en");

        Assertions.assertEquals("yea yea %test_test% yea yea",
                provider.getMessage(entity, "stack_message"));
    }

    public static class SelfReferredPlaceholderProvider extends PlaceholderProvider<ConsoleEntity> {

        public SelfReferredPlaceholderProvider() {
            super("test");
        }

        @Override
        public @Nullable String replace(ConsoleEntity entity, String parameters) {

            if (parameters.equals("test")) {
                return getMessage(entity, "stack_message");
            }

            return null;
        }

    }

}
