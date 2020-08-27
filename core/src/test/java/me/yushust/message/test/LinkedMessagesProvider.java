package me.yushust.message.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.MessageProviderBuilder;

public class LinkedMessagesProvider extends MessageProviderTestCase {
   
    @Test
    public void test() {

        MessageProvider<ConsoleEntity> provider = MessageProviderBuilder.<ConsoleEntity>create()
                .setLoadSource(loadSource)
                .setNodeFileLoader(fileLoader)
                .setFileFormat("lang_%lang%.properties")
                .build();

        ConsoleEntity entity = new ConsoleEntity("en");
        Assertions.assertEquals("Heey, I love Burgers", provider.getMessage(entity, "love"));

        entity = new ConsoleEntity("es");
        Assertions.assertEquals("Heey, yo amo las Hamburguesas", provider.getMessage(entity, "love"));

    }
    
}