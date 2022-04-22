## Convenience Methods

There are many methods for ease the message handling in the `MessageHandler` interface.


### Obtaining messages

Methods for obtaining messages (not sending)

#### MessageHandler#get

```java
String message = messageHandler.get(entity, messagePath, /* optional, varargs */ jitEntities);
```

where `message` is the obtained message, `entity` is the used entity for placeholders and
language specification (it can be an entity that will be resolved to another entity, `messagePath`
is the message location in a file, commonly separating by dots `.` for internal sections
(depending on `NodeFile`) and `jitEntities` are the just-in-time entities, used to replace
placeholders only in that method call.

For example:

```java
MailList userMailList = mails.getRecentMails(user);
String message = messageHandler.get(user, "message.welcome", userMailList);
```

where `userMailList` is a Just-In-Time entity, and it will call a `PlaceholderProvider` registered
for type `MailList` and identifier specified in the message at `message.welcome` message


#### MessageHandler#replacing

```java
String message = messageHandler.replacing(entity, messagePath, /* var-args */ replacementPack);
```

Same as above, but replaces literal strings specified in code. Where `replacementPack` is a
var-args array of `Object`. It's a pair array of `String` and `Object`. I mean, you must add
a `String` and then an `Object`, and so on. Elements in even indexes are replaced by the elements
in odd indexes in the message. It's util for just-in-time replacements that aren't necessary
entities.

See this example in a 'command' using a command manager with parametric commands support
(like [command-flow](https://github.com/unnamed/command-flow/))

```java
// command: setmoney <money>
@Command(name = "setmoney")
void setMoney(@Sender User user, int money) {
    String message = messageHandler.replacing(
         user, "message.updated-money",
        "{{added}}", money,
        "{{total}}", user.getBalance() + money
    );
    // send the message
}
```

### Sending messages
Sending has a similar definition. They have the prefix `send` and then `replacing` (or not, if no
replacements are required), they also accept a string `mode` when the method name ends with `in`.
For example: `messageHandler.sendIn(target, "message.path", "title-mode");`