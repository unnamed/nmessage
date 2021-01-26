package me.yushust.message.test.base;

import me.yushust.message.format.PlaceholderProvider;
import me.yushust.message.source.MessageSource;
import me.yushust.message.source.MessageSourceDecorator;
import me.yushust.message.source.properties.PropertiesResourceSource;
import org.junit.jupiter.api.BeforeEach;

public class HandlerTestCase {

  protected MessageSource source;

  protected PlaceholderProvider<Entity> testProvider() {
    return (handle, entity, placeholder) -> {
      switch (placeholder) {
        case "hash":
          return entity.hashCode();
        case "val":
          return entity.toString();
        default:
          return null;
      }
    };
  }

  @BeforeEach
  public void createRepository() {
    this.source = MessageSourceDecorator
      .decorate(new PropertiesResourceSource("lang_%lang%.properties"))
      .addFallbackLanguage("en")
      .get();
  }

}
