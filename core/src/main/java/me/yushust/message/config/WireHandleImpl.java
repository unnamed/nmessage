package me.yushust.message.config;

import me.yushust.message.format.MessageInterceptor;
import me.yushust.message.util.Validate;

public class WireHandleImpl implements WireHandle {

  private final ConfigurationContainer configurationContainer = new ConfigurationContainer();
  private String startDelimiter = "%";
  private String endDelimiter = "%";

  @Override
  public WireHandle delimiting(String start, String end) {
    this.startDelimiter = start;
    this.endDelimiter = end;
    return this;
  }

  @Override
  public WireHandle intercept(MessageInterceptor interceptor) {
    Validate.isNotNull(interceptor, "interceptor");
    configurationContainer.registerInterceptor(interceptor);
    return this;
  }

  @Override
  public <E> SpecificWireHandle<E> specify(Class<E> entityType) {
    Validate.isNotNull(entityType, "entityType");
    return new SpecificWireHandle<>(
        configurationContainer,
        entityType
    );
  }

  public ConfigurationContainer getWiringContainer() {
    return configurationContainer;
  }

  public String getStartDelimiter() {
    return startDelimiter;
  }

  public String getEndDelimiter() {
    return endDelimiter;
  }

}
