## Placeholder Providers

It's responsible for getting a value using a specific 'variable' in messages
For example, if we set `{receiver_name}` in a message, we can replace it with
the receiver name using a `PlaceholderProvider`.

In that variable, the parts are identifier (`receiver`) and the parameters
(`name`), they are delimited by `_`. And we resolve that implementing a
`PlaceholderProvider`

### Definition

`PlaceholderProvider` is

```java
public interface PlaceholderProvider<E> {
    
  @Nullable
  Object replace(ContextRepository ctx, E entity, String parameters);
  
}
```
where 'E' is the used entity type. In the above example, we can use a named
class that can receive a message, for example, an `User`.

### Implementing

Implementing the placeholder provider for the above example:

```java
public class UserPlaceholderProvider implements PlaceholderProvider<User> {

    @Override
    @Nullable
    public Object replace(ContextRepository ctx, User entity, String param) {
        switch (param.toLowerCase()) {
            case "name": {
                return entity.getName();
            }
            case "age": {
                return entity.getAge();
            }
            case "another_property": {
                return entity.getAnotherProperty();
            }
            default: return null; // null indicates an invalid parameter
        }
    }

}
```
The method return type is `Object` for convenience. You can return numbers, strings,
booleans and they are converted to a string using `Object#toString`. Null indicates
that an invalid parameter was provided and the placeholder isn't replaced.

### Registration

```java
MessageHandler handler = MessageHandler.of(
    source,
    config -> {
      config.specify(User.class)
            .addProvider("user", new UserPlaceholderProvider())
            .addProvider("idk", new SomeOtherProvider());
    }
);
```