# JLL (Java Language Library) [![Build Status](https://travis-ci.com/yusshu/lang-lib.svg?branch=master)](https://travis-ci.com/yusshu/lang-lib)

A simple and a bit abstract library to handle messages with multilanguage support.
I have seen many things in languages ​​that I do not understand, to avoid this, I bring here a library for multilanguage and easy to obtain messages from configuration files or from any other place

## Usage

First, build a `MessageProvider` with a pretty builder.
Assuming that `Thing` is the receiver of the messages, who also has some properties and its language
|                           | Specification                                                                      | Implementation                                                                                                                   |
|---------------------------|-----------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| LanguageProvider          | Who provides the language of the specified propertyHolder (in this case, `Thing`) | The default implementation always returns null, so the default language will be used, you can create your own implementation     |
| LoadSource (required)     | Specifies where message files are to be loaded from                               | It is instantiated by passing it a ClassLoader (to obtain the resources) and a File (directory where the files will be obtained) |
| NodeFileLoader (required) | Loads the message files                                                           | It is responsible for loading files using InputStream or File                                                                    |
| Default Language          | The default language, used if a language file or a message isn't found            | It's a String                                                                                                                    |
| File Format               | The file format, like "lang_%lang%.properties" or "messages_%lang%.json", etc.    | It's a String, variables: `%lang%` - The language                                                                                |
| ProvideStrategy           | What to do if a message is not found                                              | Default implementations: ProvideStrategy.RETURN_PATH, Providestrategy.RETURN_NULL. You can create your own implementation        |
| MessageConsumer           | Specify how to send messages to `Thing`                                           | Default implementation does nothing                                                                                              |

Code:
```java
MessageRepository repository = MessageRepository.builder()
        .setLoadSource(
            new LoadSource(
                this.getClass().getClassLoader(),
                new File("path/to/folder")
            )
        )
        .setNodeFileLoader(nodeFileLoader)
        .setFileFormat("messages_%lang%.properties")
        .build();

MessageProvider<Thing> messageProvider = MessageProvider.<Thing>builder()
        .setRepository(repository)
		.setDefaultLanguage("en")
		.build();
```
NodeFileLoader implementation: (for Bukkit) [YamlFileLoader](https://github.com/yusshu/lang-lib/blob/master/bukkit/src/main/java/me/yushust/message/format/bukkit/yaml/YamlFileLoader.java), and NodeFileWrapper: [YamlConfigurationWrapper](https://github.com/yusshu/lang-lib/blob/master/bukkit/src/main/java/me/yushust/message/format/bukkit/yaml/YamlConfigurationWrapper.java)
NodeFileLoader implementation for Properties: (Built in) [PropertiesFileLoader](https://github.com/yusshu/lang-lib/blob/master/core/src/main/java/me/yushust/message/core/holder/defaults/PropertiesFileLoader.java), and NodeFileWrapper: [PropertiesNodeFile](https://github.com/yusshu/lang-lib/blob/master/core/src/main/java/me/yushust/message/core/holder/defaults/PropertiesNodeFile.java)
## Getting messages

There are **two ways** to get messages: using the propertyHolder and using the language.
Using the language doesn't call `MessageInterceptor`s nor `PlaceholderProvider`s.

Using the property holder:
```java
Thing thing = ...;
String message = messageProvider.getMessage(thing, "message.path");
thing.send(message);
```

Using the language:
```java
String language = ...; // if the language is null, the default language will be used
String message = messageProvider.getMessage(language, "message.path");
System.out.println(message);
```
## Implementing LanguageProvider
Implementing `LanguageProvider` is **very simple**, you just have to implement a method that returns the language of the specified propertyHolder. **Example:**
```java
public class ThingLanguageProvider implements LanguageProvider<Thing> {

	@Override
	@Nullable
	public String getLanguage(Thing thing) {
		return thing.getLanguage();
	}

}
```
Using lambda method references (Java 8+):
```java
LanguageProvider<Thing> thingLanguageProvider = Thing::getLanguage;
```
Registering your `LanguageProvider`:
```java
MessageProvider<Thing> messageProvider = MessageProviderBuilder.create()
		.setRepository(repository)
		.setLanguageProvider(new ThingLanguageProvider())
		.build();
```
## Intercepting messages

Intercepting messages is as easy as modifying a String, we just implement `MessageInterceptor` or `PlaceholderProvider`.
`MessageInterceptor` receives the original text, `PlaceholderProvider` only receives the used placeholder.

**Examples:**
A name placeholder provider
```java
public class ThingPlaceholderReplacer implements PlaceholderProvider<Thing> {

    @Override
    public String[] getPlaceholders() {
        return new String[] {"name"};
    }

	@Override
	public String replace(InterceptContext<Thing> context, String placeholder) {
        return context.getEntity().getName();
	}
	
}
```
Registering the `PlaceholderProvider`:
```java
MessageProvider<Thing> messageProvider = MessageProviderBuilder.create()
	.setRepository(repository);
	.addReplacer(new ThingPlaceholderProvider())
	.build();
```

## Sending messages with MessageConsumer

You can use `MessageProvider` to **send messages**, but this only works if you have previously specified a `MessageConsumer`

**Implementing MessageConsumer:**
It simply takes care of sending the already formatted message.
```java
public class ThingMessageConsumer implements MessageConsumer<Thing> {
    
    @Override
    public void sendMessage(Thing receiver, String message) {
		receiver.sendMessage(message);
	}
	
}
```
Or using lambda method references (Java 8+)
```java
MessageConsumer<Thing> messageConsumer = Thing::sendMessage;
```
Registering your MessageConsumer:
```java
MessageProvider<Thing> messageProvider = MessageProviderBuilder.create()
	.setRepository(repository)
	.setMessageConsumer(new ThingMessageConsumer())
	.build();
```

**Sending the message**
Just use
```java
Thing thing = ...;
messageProvider.sendMessage(thing, "message.path");
```
Or
```java
List<Thing> things = ...; // it can be any iterable (Collection, etc)
messageProvider.sendMessage(things, "message.path");
```
And using a just-in-time interceptor:
```java
List<Thing> things = ...;
messageProvider.sendMessage(things, "message.path", message -> {
	return message.replace("{{things}}", String.valueOf(things.size()));
});
```
## Implementing a NodeFile
A `NodeFile` will be a file already loaded from where the messages will be obtained.
...
## Installation
Maven repository
```xml
<repository>
	<id>unnamed-releases</id>
	<url>https://repo.unnamed.team/repository/unnamed-releases</url>
</repository>
```
Maven dependency
```xml
<dependency>
	<groupId>me.yushust.message</groupId>
	<artifactId>core</artifactId>
	<version>VERSION</version>
</dependency>
```

Bukkit adapters maven dependency
(Adds support for YML using Bukkit's `YamlConfiguration`)
```xml
<dependency>
	<groupId>me.yushust.message</groupId>
	<artifactId>bukkit-formatters</artifactId>
	<version>VERSION</version>
</dependency>
```
> "Yes."
