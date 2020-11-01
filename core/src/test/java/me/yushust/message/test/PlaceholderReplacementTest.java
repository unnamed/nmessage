package me.yushust.message.test;

import me.yushust.message.MessageHandler;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceholderReplacementTest extends HandlerTestCase {

  @Test
  public void test() {

    MessageHandler<Entity> handler = MessageHandler.builder(Entity.class)
        .setRepository(repository)
        .addProvider("obj", (handle, entity, placeholder) -> {
          switch (placeholder) {
            case "hash":
              return entity.hashCode();
            case "val":
              return entity.toString();
            default:
              return null;
          }
        })
        .build();

    Entity entity = new Entity();
    Assertions.assertEquals("Your object hashcode is 0 and as string it's i'manentity",
        handler.get(entity, "replace-this"));

  }

}
