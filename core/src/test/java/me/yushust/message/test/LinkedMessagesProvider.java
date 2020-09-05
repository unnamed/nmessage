package me.yushust.message.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.yushust.message.MessageHandler;

public class LinkedMessagesProvider extends MessageProviderTestCase {
   
    @Test
    public void test() {

        MessageHandler<ConsoleEntity> provider = MessageHandler.<ConsoleEntity>builder()
                .setRepository(messageRepository)
                .setLanguageProvider(ConsoleEntity::getLanguage)
                .build();

        ConsoleEntity entity = new ConsoleEntity("en");
        Assertions.assertEquals("Heeey, I love Burgers", provider.getMessage(entity, "love"));

        entity = new ConsoleEntity("es");
        Assertions.assertEquals("Heeey, yo amo las Hamburguesas", provider.getMessage(entity, "love"));

    }
    
}