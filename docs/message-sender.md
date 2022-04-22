# Message Sender

It sends messages to a specific entity according to a mode

### Definition

```java
public interface MessageSender<E> {
    
  default void send(E receiver, String mode, StringList messages) {
    send(receiver, mode, messages.join("\n"));
  }
  
  void send(E receiver, String mode, String message);

}
```
The method provides the `receiver` (the entity that must receive the given `message`),
the `mode` (how to send the message, this will be explained later), and the sent `message`.

### Implementation

```java
public class UserMessageSender implements MessageSender<User> {
  @Override
  public void send(User receiver, String mode, String message) {
    // you can also add "outputs" (ex. window message, console message, etc)
    switch (mode) {
      case "info":
        receiver.send(message);
        break;
      case "warn":
        receiver.warn(message);
        break;
      case "severe":
        receiver.severe(message);
        break;
    }
  }
}
```

### Configuring

Set the `Messenger` for a specific type while constructing `MessageHandler`

```java
MessageHandler handler = MessageHandler.of(
    source,
    config -> {
      // set the message sender
      config.specify(User.class)
            .setMessageSender(new UserMessageSender())
            // it's a fluent API, you can also set the linguist here
            .setLinguist(new UserLinguist());
    }
);
```