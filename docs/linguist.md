## Linguist

It is responsible for obtaining the language of a specific entity

### Definition

```java
public interface Linguist<E> {
  @Nullable
  String getLanguage(E entity);
}
```

You must implement the interface and set it to the `MessageHandler` to use it.
If the `getLanguage` method returns null, the default language is used.

### Implementation

This is an example of an imaginary class 'User'

```java
public class UserLinguist implements Linguist<User> {
  @Override
  @Nullable
  public String getLanguage(User entity) {
    // get the language of "entity" and return it
  }
}
```

### Configuring

```java
MessageHandler handler = MessageHandler.of(
    source,
    config -> {

      // using setLinguist(Linguist)
      config.specify(User.class)
            .setLinguist(new UserLinguist());

      // you can specify multiple linguist for
      // specific entity types
      config.specify(SomeOtherEntity.class)
            .setLinguist(new SomeOtherEntityLinguist());

      // you can also use lambda method references for this
      config.specify(ExampleUsingLambdaEntity.class)
            .setLinguist(ExampleUsingLambdaEntity::getLanguage);
    }
);
```