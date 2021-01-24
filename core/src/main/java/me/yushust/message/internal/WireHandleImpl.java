package me.yushust.message.internal;

import me.yushust.message.config.SpecificWireHandle;
import me.yushust.message.config.WireHandle;
import me.yushust.message.config.WiringContainer;
import me.yushust.message.util.Validate;

public class WireHandleImpl implements WireHandle {

  private final WiringContainer wiringContainer = new WiringContainer();
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
    wiringContainer.registerInterceptor(interceptor);
    return this;
  }

  @Override
  public <E> SpecificWireHandle<E> specify(Class<E> entityType) {
    Validate.isNotNull(entityType, "entityType");
    return new SpecificWireHandle<>(
        wiringContainer,
        entityType
    );
  }

  public WiringContainer getWiringContainer() {
    return wiringContainer;
  }

  public String getStartDelimiter() {
    return startDelimiter;
  }

  public String getEndDelimiter() {
    return endDelimiter;
  }

}
