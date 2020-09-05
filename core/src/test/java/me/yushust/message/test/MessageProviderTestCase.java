package me.yushust.message.test;

import java.io.File;

import me.yushust.message.MessageRepository;

import org.junit.jupiter.api.BeforeEach;

import me.yushust.message.holder.LoadSource;
import me.yushust.message.holder.defaults.PropertiesFileLoader;

public class MessageProviderTestCase {

    protected MessageRepository messageRepository;

    @BeforeEach
    public void prepare() throws Exception {

        File folder = new File(
            this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
        );

        this.messageRepository = MessageRepository.builder()
                .setLoadSource(
                        new LoadSource(
                                this.getClass().getClassLoader(),
                                folder
                        )
                )
                .setNodeFileLoader(new PropertiesFileLoader())
                .build();

    }

}