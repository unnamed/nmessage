package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageProvider;
import me.yushust.message.model.Text;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TextModelTest extends HandlerTestCase {

    @Test
    public void test() {

        MessageProvider provider = MessageProvider.create(
                source,
                wiring -> wiring.specify(Entity.class)
                        .addProvider("obj", testProvider())
        );

        Entity entity = new Entity();
        String expected = "https://github.com/yusshu 'i'manentity' <- Peruvian at 100 meters";
        Text text1 = Text.of("from-text")
                .replace("%distance%", 100)
                .withEntities(entity);
        Assertions.assertEquals(
                expected,
                provider.format(entity, text1)
        );

        createRepository();
        MessageHandler handler = MessageHandler.of(source,
                handle -> handle.specify(Entity.class)
                        .setMessageSender((receiver, mode, message) -> {
                            if ("lowercase".equals(mode)) {
                                Assertions.assertEquals(
                                        "check my new project -> https://github.com/unnamed/mappa",
                                        message.toLowerCase()
                                );
                            } else if ("uppercase".equals(mode)) {
                                Assertions.assertEquals(
                                        "CHECK MY NEW PROJECT -> HTTPS://GITHUB.COM/UNNAMED/MAPPA",
                                        message.toUpperCase()
                                );
                            }
                        }));
        Text text2 = Text.of("another-text")
                .replace("%project%", "https://github.com/unnamed/mappa")
                .mode("lowercase");
        Text text3 = Text.of(text2)
                .mode("uppercase");
        handler.send(entity, text2);
        handler.send(entity, text3);
    }
}
