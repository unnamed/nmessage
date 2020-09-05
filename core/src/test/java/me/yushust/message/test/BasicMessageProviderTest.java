package me.yushust.message.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.yushust.message.MessageHandler;

public class BasicMessageProviderTest extends MessageProviderTestCase {

    @Test
    public void test() {

        MessageHandler<ConsoleEntity> provider = MessageHandler.<ConsoleEntity>builder()
                .setRepository(messageRepository)
                .setLanguageProvider(ConsoleEntity::getLanguage)
                .build();

        ConsoleEntity entity = new ConsoleEntity("en");
        Assertions.assertEquals("Hello world!!", provider.getMessage(entity, "hello_world"));

        entity = new ConsoleEntity("es");
        Assertions.assertEquals("Hola mundo!!", provider.getMessage(entity, "hello_world"));

        entity = new ConsoleEntity("fr");
        // gets the message using the default language
        Assertions.assertEquals("Hello world!!", provider.getMessage(entity, "hello_world"));

    }

}