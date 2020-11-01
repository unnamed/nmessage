package me.yushust.message.strategy;

public enum Notify {

  /* This class doesn't have instances*/;

  public enum Failure {
    PROVIDER_NOT_FOUND,
    PROVIDER_NOT_APPLICABLE,
    INVALID_RETURN_VALUE
  }

  public enum Warning {
    CYCLIC_LINKED_MESSAGES
  }
}
