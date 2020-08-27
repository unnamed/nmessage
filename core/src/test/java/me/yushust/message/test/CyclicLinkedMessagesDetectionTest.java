package me.yushust.message.test;

import me.yushust.message.core.MessageProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CyclicLinkedMessagesDetectionTest extends MessageProviderTestCase {

    @Test
    public void test() {

        MessageProvider<ConsoleEntity> provider = MessageProvider.<ConsoleEntity>builder()
                .setRepository(messageRepository)
                .setLanguageProvider(ConsoleEntity::getLanguage)
                .build();

        ConsoleEntity entity = new ConsoleEntity("en");
        Assertions.assertEquals("Foo bar %path_bruh% yes foo", provider.getMessage(entity, "bruh"));

    }

}
