package me.yushust.message.test;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;

import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.holder.defaults.PropertiesFileLoader;

public class MessageProviderTestCase {
    
    protected LoadSource loadSource;
    protected NodeFileLoader fileLoader;

    @BeforeEach
    public void prepare() throws Exception {

        File folder = new File(
            this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
        );

        this.loadSource = new LoadSource(
            this.getClass().getClassLoader(),
            folder
        );

        this.fileLoader = new PropertiesFileLoader(folder);

    }

}