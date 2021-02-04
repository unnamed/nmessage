package me.yushust.message.test;

import me.yushust.message.MessageProvider;
import me.yushust.message.test.base.Entity;
import me.yushust.message.test.base.HandlerTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JitEntitiesFormattingTest extends HandlerTestCase {

  @Test
  public void test() {

    MessageProvider handler = MessageProvider.create(
        source,
        wiring -> wiring
          .specify(NamedEntity.class)
          .addProvider("ent", (ctx, entity, param) -> entity.name)
    );

    Entity entity = new Entity();
    NamedEntity namedEntity = new NamedEntity();
    namedEntity.name = "OcNo";

    Assertions.assertEquals(
      "The name of the external entity is OcNo",
      handler.get(entity, "jit-entity-test", namedEntity)
    );

    NamedEntity namedEntity2 = new NamedEntity();
    namedEntity2.name = "Fixed";

    Assertions.assertEquals(
      "The name of the external entity is Fixed",
      handler.get(entity, "jit-entity-test", namedEntity2)
    );

  }

  private static class NamedEntity {
    private String name;
  }

}
