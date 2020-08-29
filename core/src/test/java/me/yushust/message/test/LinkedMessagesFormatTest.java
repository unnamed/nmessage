package me.yushust.message.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.yushust.message.core.MessageProvider;

public class LinkedMessagesFormatTest extends MessageProviderTestCase {
    
    @Test
    public void test() {
        
        MessageProvider<ConsoleEntity> provider = MessageProvider.<ConsoleEntity>builder()
                .setRepository(messageRepository)
                .setLanguageProvider(ConsoleEntity::getLanguage)
                .addPlaceholderReplacer(new HashCodePlaceholderProvider())
                .build();

        ConsoleEntity entity = new ConsoleEntity("en");
        Assertions.assertEquals("", provider.getMessage(entity, "that"));

    }

}