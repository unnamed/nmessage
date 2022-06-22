package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceholderReplacementTest extends HandlerTestCase {

    @Test
    public void test() {

        MessageHandler handler = MessageHandler.of(
                source,
                wiring -> wiring
                        .specify(Entity.class)
                        .addProvider("obj", testProvider())
                        .setMessageSender((entity, mode, message) -> {
                            Assertions.assertEquals(
                                    "Your object hashcode is 0 and as string it's i'manentity",
                                    message
                            );
                        })
        );

        Assertions.assertEquals(
                "Test this i'manentity bruh",
                handler.get(
                        new Object(), "test-this",
                        new Entity()
                )
        );

        Entity entity = new Entity();
        Assertions.assertEquals(
                "Your object hashcode is 0 and as string it's i'manentity",
                handler.replacing(
                        entity, "replace-this",
                        "%thing%", "object"
                )
        );
        handler.sendReplacing(
                entity, "replace-this",
                "%thing%", "object"
        );
    }

}
