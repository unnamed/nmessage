package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.source.NodeFile;
import me.yushust.message.test.base.HandlerTestCase;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Test extends HandlerTestCase {

  @org.junit.jupiter.api.Test
  public void test() {
    MessageHandler messageHandler = MessageHandler.create(
        MessageProvider.builder()
            .setLoadSource(new LoadSource(getClass().getClassLoader(), new File("/")))
            .setNodeFileLoader(new NodeFileLoader() {
              @Override
              public NodeFile load(LoadSource source, File file) {
                return node -> new ArrayList<>(Arrays.asList("hello world %test_pito%", "%test_pito%", "%test_pito%"));
              }

              @Override
              public NodeFile loadAndCreate(LoadSource source, InputStream inputStream, String fileName) {
                return load(null, null);
              }
            })
            .setFileFormat("messages_%lang%.properties")
            .build(),
        wiring -> {
          wiring.specify(Object.class)
              .addProvider("test", (ctx, entity, placeholder) -> "ola")
              .setMessenger((m, md, ms) -> System.out.println(ms));
        }
    );

    messageHandler.send(new Object(), "pito");
  }

}
