package me.yushust.message.test;

import me.yushust.message.MessageProvider;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CyclicMessagesTest extends HandlerTestCase {

  @Test
  public void test() {

    MessageProvider handler = MessageProvider.create(source);

    Assertions.assertEquals("Check Check Check Check %path_linked-message-2%",
        handler.get(null, "linked-message-1"));

  }

}
