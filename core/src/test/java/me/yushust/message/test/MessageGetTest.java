package me.yushust.message.test;

import me.yushust.message.MessageProvider;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageGetTest extends HandlerTestCase {

    @Test
    public void test() {
        MessageProvider messageProvider = MessageProvider.create(source);

        Assertions.assertEquals("I don't know", messageProvider.get(null, "message-get-test"));
    }

}
