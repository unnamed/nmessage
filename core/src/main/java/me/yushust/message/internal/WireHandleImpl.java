package me.yushust.message.internal;

import me.yushust.message.MessageInterceptor;
import me.yushust.message.config.SpecificWireHandle;
import me.yushust.message.config.WireHandle;
import me.yushust.message.config.WiringContainer;
import me.yushust.message.mode.Mode;
import me.yushust.message.util.Validate;

public class WireHandleImpl implements WireHandle {

  private final WiringContainer wiringContainer = new WiringContainer();
  private Class<? extends Mode> modesType;
  private Mode defaultMode;
  private char startDelimiter = '%';
  private char endDelimiter = '%';

  @Override
  public <M extends Enum<M> & Mode> WireHandle withModes(Class<M> modesClass) {
    Validate.notNull(modesClass, "modesClass");
    this.modesType = modesClass;
    int defaultCount = 0;
    for (Mode mode : modesClass.getEnumConstants()) {
      if (mode.isDefault()) {
        this.defaultMode = mode;
        defaultCount++;
      }
    }
    if (defaultCount == 0) {
      throw new IllegalArgumentException("Enum with modes doesn't contain a default mode!");
    } else if (defaultCount != 1) {
      throw new IllegalArgumentException("Enum contains more than 1 default mode!");
    }
    return this;
  }

  @Override
  public WireHandle delimiting(char start, char end) {
    this.startDelimiter = start;
    this.endDelimiter = end;
    return this;
  }

  @Override
  public WireHandle intercept(MessageInterceptor interceptor) {
    Validate.notNull(interceptor, "interceptor");
    wiringContainer.registerInterceptor(interceptor);
    return this;
  }

  @Override
  public <E> SpecificWireHandle<E> specify(Class<E> entityType) {
    Validate.notNull(entityType, "entityType");
    return new SpecificWireHandle<>(
        wiringContainer,
        entityType
    );
  }

  public WiringContainer getWiringContainer() {
    return wiringContainer;
  }

  public Class<? extends Mode> getModesType() {
    return modesType;
  }

  public Mode getDefaultMode() {
    return defaultMode;
  }

  public char getStartDelimiter() {
    return startDelimiter;
  }

  public char getEndDelimiter() {
    return endDelimiter;
  }

}
