package me.yushust.message.test;

import me.yushust.message.core.MessageProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceholderProviderTest extends MessageProviderTestCase {

    @Test
    public void test() {

        MessageProvider<ConsoleEntity> provider = MessageProvider.<ConsoleEntity>builder()
                .setRepository(messageRepository)
                .setLanguageProvider(ConsoleEntity::getLanguage)
                .addPlaceholderReplacer(new HashCodePlaceholderProvider())
                .build();


        ConsoleEntity entity = new ConsoleEntity("en");
        Assertions.assertEquals("Heey, this is your hashcode " + entity.hashCode(),
                provider.getMessage(entity, "hashcode"));

        entity = new ConsoleEntity("es");
        System.out.println(provider.getMessage(entity, "hashcode"));
        Assertions.assertEquals("Heey, aquí está tu hashcode " + entity.hashCode(),
                provider.getMessage(entity, "hashcode"));

    }

}
