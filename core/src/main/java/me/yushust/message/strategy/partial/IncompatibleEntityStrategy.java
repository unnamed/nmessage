package me.yushust.message.strategy.partial;

public interface IncompatibleEntityStrategy {

  void incompatibleEntity(Object fakeEntity);

  void failSendMessage(Object fakeEntity, String message);

}
