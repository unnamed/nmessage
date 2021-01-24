package me.yushust.message.test.base;

import me.yushust.message.specific.PlaceholderProvider;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class HandlerTestCase {

  protected MessageProvider repository;

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
    this.repository = MessageProvider.builder()
        .setDefaultLanguage("en")
        .setNodeFileLoader(NodeFileLoader.forProperties())
        .setFileFormat("messages_%lang%.properties")
        .setLoadSource(
            new LoadSource(
                getClass().getClassLoader(),
                new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile())
            )
        )
        .build();
  }

}
