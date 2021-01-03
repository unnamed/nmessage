package me.yushust.message.config;

import me.yushust.message.MessageInterceptor;
import me.yushust.message.mode.Mode;

public interface WireHandle {

  WiringContainer getWiringContainer();

  Class<? extends Enum<?>> getModesClass();

  <M extends Enum<M> & Mode> WireHandle withModes(Class<M> modesClass);

  String getStartDelimiter();

  String getEndDelimiter();

  WireHandle delimiting(String start, String end);

  WireHandle intercept(MessageInterceptor interceptor);

  <E> SpecificWireHandle<E> specify(Class<E> entityType);

  default void install(Specifier... specifiers) {
    for (Specifier specifier : specifiers) {
      specifier.configure(this);
    }
  }

}
