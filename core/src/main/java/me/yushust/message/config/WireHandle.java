package me.yushust.message.config;

import me.yushust.message.internal.MessageInterceptor;

public interface WireHandle {

  WiringContainer getWiringContainer();

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
