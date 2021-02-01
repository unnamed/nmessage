package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.MessageProvider;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractEntityTypeTest extends HandlerTestCase {

  @Test
  public void test() {
    MessageHandler provider = MessageHandler.of(MessageProvider.create(
      source,
      config -> config.specify(Foo.class)
        .setMessageSender((entity, mode, message) -> {})
    ));

    Foo entity = new Baz();
    try {
      provider.send(entity, "message-get-test");
    } catch (Throwable error) {
      Assertions.fail(error);
    }
  }

  public interface Foo {
  }

  public interface Blah extends Foo {
  }

  public static class Bar implements Blah {
  }

  public static class Boo extends Bar {
  }

  public static class Baz extends Boo implements Blah {
  }

}
