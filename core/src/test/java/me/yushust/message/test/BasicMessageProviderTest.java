package me.yushust.message.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.MessageProviderBuilder;

public class BasicMessageProviderTest extends MessageProviderTestCase {

    @Test
    public void test() {

        MessageProvider<ConsoleEntity> provider = MessageProviderBuilder.<ConsoleEntity>create()
                .setLoadSource(loadSource)
                .setDefaultLanguage("en")
                .setFileFormat("lang_%lang%.properties")
                .setNodeFileLoader(fileLoader)
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