package me.yushust.message;

import me.yushust.message.holder.LoadSource;
import me.yushust.message.holder.NodeFileLoader;
import me.yushust.message.holder.allocate.SimpleFileAllocator;
import me.yushust.message.internal.SimpleMessageRepository;
import me.yushust.message.util.Validate;

/**
 * A fluent {@link MessageRepository} builder,
 * create a builder using {@link MessageRepository#builder}
 */
public final class MessageRepositoryBuilder {

    /**
     * Where the message files will be loaded from.
     * (It's required to set a {@link LoadSource} using
     * {@link MessageRepositoryBuilder#setLoadSource})
     */
    private LoadSource loadSource;

    /**
     * How the message files will be loaded.
     * (it's required to set a {@link NodeFileLoader}
     * using {@link MessageRepositoryBuilder#setNodeFileLoader})
     */
    private NodeFileLoader nodeFileLoader;

    private ProvideStrategy provideStrategy = ProvideStrategy.RETURN_PATH;
    private String fileFormat = "lang_%lang%.properties";
    private String defaultLanguage = "en";

    MessageRepositoryBuilder() {
    }

    public MessageRepositoryBuilder setLoadSource(LoadSource loadSource) {
        this.loadSource = Validate.notNull(loadSource);
        return this;
    }

    public MessageRepositoryBuilder setNodeFileLoader(NodeFileLoader nodeFileLoader) {
        this.nodeFileLoader = Validate.notNull(nodeFileLoader);
        return this;
    }

    public MessageRepositoryBuilder setProvideStrategy(ProvideStrategy provideStrategy) {
        this.provideStrategy = Validate.notNull(provideStrategy);
        return this;
    }

    public MessageRepositoryBuilder setFileFormat(String fileFormat) {
        this.fileFormat = Validate.notEmpty(fileFormat);
        return this;
    }

    public MessageRepositoryBuilder setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = Validate.notEmpty(defaultLanguage);
        return this;
    }

    public MessageRepository build() {

        Validate.state(
                nodeFileLoader != null && loadSource != null,
                "The nodeFileLoader and the loadSource must be setted!"
        );

        return new SimpleMessageRepository(
                new SimpleFileAllocator(nodeFileLoader, loadSource),
                provideStrategy,
                fileFormat,
                defaultLanguage
        );
    }

}
