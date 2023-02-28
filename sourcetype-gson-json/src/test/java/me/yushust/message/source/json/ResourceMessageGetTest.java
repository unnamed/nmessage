package me.yushust.message.source.json;

import me.yushust.message.MessageProvider;
import me.yushust.message.config.ConfigurationHandle;
import me.yushust.message.impl.MessageProviderImpl;
import me.yushust.message.source.MessageSource;
import me.yushust.message.source.MessageSourceDecorator;
import me.yushust.message.source.gson.JsonResourceSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceMessageGetTest {

    @Test
    public void test() {
        MessageSource source = MessageSourceDecorator
                .decorate(new JsonResourceSource(
                        getClass().getClassLoader(),
                        "test_%lang%.json"
                )).addFallbackLanguage("en")
                .get();

        MessageProvider provider = new MessageProviderImpl(source, new ConfigurationHandle());

        assertEquals("nefasto", provider.get(null, "test1"));
        assertEquals("terrible", provider.get(null, "test2"));
        assertEquals("oremos", provider.get(null, "test3"));
        assertEquals("áéíóú ñ §æµŋnamed", provider.get(null, "test4.test5"));
    }
}
