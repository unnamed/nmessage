package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntitySpecificityTest extends HandlerTestCase {

    @Test
    public void test() {
        MessageHandler messageHandler = MessageHandler.of(
                source,
                config -> {
                    config.specify(B.class).setLinguist(b -> "b");
                    config.specify(A.class).setLinguist(a -> "a");
                }
        );

        A a = new AA();
        B b = new BB();

        Assertions.assertEquals("a", messageHandler.getLanguage(a));
        Assertions.assertEquals("b", messageHandler.getLanguage(b));
    }

    public interface A {

    }

    public interface B extends A {

    }

    public static class BB implements B {

    }

    public static class AA implements A {

    }

}
