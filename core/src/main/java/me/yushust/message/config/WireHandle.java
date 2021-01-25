package me.yushust.message.config;

import me.yushust.message.format.MessageInterceptor;

public interface WireHandle {

  ConfigurationContainer getWiringContainer();

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
