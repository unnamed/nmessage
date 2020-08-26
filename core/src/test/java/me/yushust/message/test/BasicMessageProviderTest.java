package me.yushust.message.test;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import me.yushust.message.core.MessageProvider;
import me.yushust.message.core.MessageProviderBuilder;
import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.defaults.PropertiesFileLoader;

public class BasicMessageProviderTest {

    @Test
    public void test() throws Exception {

        File folder = new File(
            this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
        );

        MessageProvider<ConsoleEntity> provider = MessageProviderBuilder.<ConsoleEntity>create()
                .setLoadSource(new LoadSource(
                    this.getClass().getClassLoader(),
                    folder
                ))
                .setDefaultLanguage("en")
                .setFileFormat("lang_%lang%.properties")
                .setNodeFileLoader(new PropertiesFileLoader(folder))
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