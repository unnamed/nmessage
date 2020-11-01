package me.yushust.message.internal;

import me.yushust.message.MessageRepository;
import me.yushust.message.strategy.Strategy;
import me.yushust.message.file.LoadSource;
import me.yushust.message.file.NodeFileLoader;
import me.yushust.message.util.Validate;

import java.util.function.UnaryOperator;

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
  LoadSource loadSource;

  /**
   * How the message files will be loaded.
   * (it's required to set a {@link NodeFileLoader}
   * using {@link MessageRepositoryBuilder#setNodeFileLoader})
   */
  NodeFileLoader nodeFileLoader;

  Strategy strategy = new Strategy();
  String fileFormat = "lang_%lang%.properties";
  String defaultLanguage = "en";

  public MessageRepositoryBuilder setLoadSource(LoadSource loadSource) {
    this.loadSource = Validate.notNull(loadSource);
    return this;
  }

  public MessageRepositoryBuilder setNodeFileLoader(NodeFileLoader nodeFileLoader) {
    this.nodeFileLoader = Validate.notNull(nodeFileLoader);
    return this;
  }

  public MessageRepositoryBuilder applyToStrategy(UnaryOperator<Strategy> strategyInterceptor) {
    this.strategy = Validate.notNull(
        strategyInterceptor.apply(strategy)
    );
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

    return new MessageRepositoryImpl(this);
  }

}
