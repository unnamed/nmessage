package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageProvider;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceholderReplacingTest extends HandlerTestCase {

    @Test
    public void test() {
        MessageHandler messageHandler = MessageHandler.of(
                MessageProvider.create(
                        source,
                        config -> {
                            config.delimiting("${{", "}}");
                            config.specify(Object.class)
                                    .addProvider("test", (ctx, obj, param) -> param)
                                    .addProvider("hash", (ctx, obj, param) -> obj.hashCode());
                        }
                )
        );

        Object e = new Object();
        int hash = e.hashCode();

        Assertions.assertEquals(
                "This is the hash " + hash + " and the param test",
                messageHandler.format(e, "This is the hash ${{hash_d}} and the param ${{test_test}}")
        );

        Assertions.assertEquals(
                "This text ${{is_not_replaced}} ${{bruh}}",
                messageHandler.format(e, "This text ${{is_not_replaced}} ${{bruh}}")
        );
    }

}
