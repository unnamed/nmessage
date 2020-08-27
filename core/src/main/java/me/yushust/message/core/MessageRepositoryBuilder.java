package me.yushust.message.core;

import me.yushust.message.core.holder.LoadSource;
import me.yushust.message.core.holder.NodeFileLoader;
import me.yushust.message.core.holder.allocate.SimpleFileAllocator;
import me.yushust.message.core.internal.SimpleMessageRepository;

import static java.util.Objects.requireNonNull;

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
        requireNonNull(loadSource);
        this.loadSource = loadSource;
        return this;
    }

    public MessageRepositoryBuilder setNodeFileLoader(NodeFileLoader nodeFileLoader) {
        requireNonNull(nodeFileLoader);
        this.nodeFileLoader = nodeFileLoader;
        return this;
    }

    public MessageRepositoryBuilder setProvideStrategy(ProvideStrategy provideStrategy) {
        requireNonNull(provideStrategy);
        this.provideStrategy = provideStrategy;
        return this;
    }

    public MessageRepositoryBuilder setFileFormat(String fileFormat) {
        requireNonNull(fileFormat);
        this.fileFormat = fileFormat;
        return this;
    }

    public MessageRepositoryBuilder setDefaultLanguage(String defaultLanguage) {
        requireNonNull(defaultLanguage);
        this.defaultLanguage = defaultLanguage;
        return this;
    }

    public MessageRepository build() {

        if (nodeFileLoader == null || loadSource == null) {
            throw new IllegalStateException("The nodeFileLoader and the loadSource aren't setted!!");
        }

        return new SimpleMessageRepository(
                new SimpleFileAllocator(nodeFileLoader, loadSource),
                provideStrategy,
                fileFormat,
                defaultLanguage
        );
    }

}
