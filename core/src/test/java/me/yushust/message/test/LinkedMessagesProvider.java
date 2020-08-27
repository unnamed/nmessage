package me.yushust.message.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.yushust.message.core.MessageProvider;

public class LinkedMessagesProvider extends MessageProviderTestCase {
   
    @Test
    public void test() {

        MessageProvider<ConsoleEntity> provider = MessageProvider.<ConsoleEntity>builder()
                .setRepository(messageRepository)
                .build();

        ConsoleEntity entity = new ConsoleEntity("en");
        Assertions.assertEquals("Heey, I love Burgers", provider.getMessage(entity, "love"));

        entity = new ConsoleEntity("es");
        Assertions.assertEquals("Heey, yo amo las Hamburguesas", provider.getMessage(entity, "love"));

    }
    
}