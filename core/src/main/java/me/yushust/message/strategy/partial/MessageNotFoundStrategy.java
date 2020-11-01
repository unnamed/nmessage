package me.yushust.message.strategy.partial;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface MessageNotFoundStrategy {

  MessageNotFoundStrategy RETURN_PATH = (language, path) -> path;

  MessageNotFoundStrategy RETURN_NULL = (language, path) -> null;

  @Nullable
  String getNotFoundMessage(String language, String path);

}
