## Placeholders

We can specify placeholder delimiters while creating the `MessageHandler`. Default
delimiters are `%` for start, and `%` for end (same characters)

```java
MessageHandler handler = MessageHandler.of(
    source,
    config -> {
        // format is: "{{identifier_parameter}}"
        config.delimiting(
          "{{", // start
          "}}" // end
        );
    }
);
```