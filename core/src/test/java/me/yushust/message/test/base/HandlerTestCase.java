package me.yushust.message.test.base;

import me.yushust.message.MessageRepository;
import me.yushust.message.file.LoadSource;
import me.yushust.message.file.NodeFileLoader;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

public class HandlerTestCase {

  protected MessageRepository repository;

  @BeforeEach
  public void createRepository() {
    this.repository = MessageRepository.builder()
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
