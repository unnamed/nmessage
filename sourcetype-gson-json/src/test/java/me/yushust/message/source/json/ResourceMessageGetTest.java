package me.yushust.message.source.json;

import me.yushust.message.MessageProvider;
import me.yushust.message.config.ConfigurationHandle;
import me.yushust.message.impl.MessageProviderImpl;
import me.yushust.message.source.MessageSource;
import me.yushust.message.source.MessageSourceDecorator;
import me.yushust.message.source.gson.JsonResourceSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceMessageGetTest {

    private final MessageSource source;
    private final MessageProvider provider;

    public ResourceMessageGetTest() {
        this.source = MessageSourceDecorator
                .decorate(new JsonResourceSource(
                        getClass().getClassLoader(),
                        "test_%lang%.json"
                )).addFallbackLanguage("en")
                .get();
        this.provider = new MessageProviderImpl(source, new ConfigurationHandle());
    }

    @Test
    public void test() {
        assertEquals("nefasto", provider.get(null, "test1"));
        assertEquals("terrible", provider.get(null, "test2"));
        assertEquals("oremos", provider.get(null, "test3"));
        assertEquals("áéíóú ñ §æµŋnamed", provider.get(null, "test4.test5"));
        assertEquals(Arrays.asList(
                "hey", "you",
                "i", "love", "you,",
                "you", "are", "worth", "of love"
        ), provider.getMany(null, "testList"));
    }

    @Test
    @DisplayName("Test message not found")
    public void test_message_not_found() {
        assertEquals("thisdoesnotexist", provider.get(null, "thisdoesnotexist"));
        assertEquals("this.does.not.exist", provider.get(null, "this.does.not.exist"));
        assertEquals("test4.thisdoesnotexist", provider.get(null, "test4.thisdoesnotexist"));
        assertEquals("test4.this.does.not.exist", provider.get(null, "test4.this.does.not.exist"));
    }

}
