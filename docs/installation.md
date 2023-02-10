## Installation

You can add nmessage to your project using [Gradle](https://gradle.org/)
*(recommended)*, [Maven](https://maven.apache.org/) or manually downloading
the JAR files


### Gradle

Add our repository

```kotlin
repositories {
    maven("https://repo.unnamed.team/repository/unnamed-public/")
}
```

Add the necessary dependencies

```kotlin
dependencies {
    // Core API, necessary for everything
    implementation("me.yushust.message:core:7.1.0")
    
    // .PROPERTIES source type
    implementation("me.yushust.message:sourcetype-properties:7.1.0")
    
    // .JSON source type
    implementation("me.yushust.message:sourcetype-gson-json:7.1.0")
  
    // Bukkit YAML source type and helpers
    implementation("me.yushust.message:sourcetype-bukkit-yml:7.1.0")
    
    // BungeeCord YAML source type and helpers
    implementation("me.yushust.message:sourcetype-bungee-yml:7.1.0")
}
```

### Maven

Add our repository

```xml
<repository>
    <id>unnamed-public</id>
    <url>https://repo.unnamed.team/repository/unnamed-public/</url>
</repository>
```

Add the necessary dependencies

```xml
<!-- Core API, necessary for everything -->
<dependency>
    <groupId>me.yushust.message</groupId>
    <artifactId>core</artifactId>
    <version>7.1.0</version>
</dependency>

<!-- .PROPERTIES source type -->
<dependency>
    <groupId>me.yushust.message</groupId>
    <artifactId>sourcetype-properties</artifactId>
    <version>7.1.0</version>
</dependency>

<!-- .JSON source type -->
<dependency>
    <groupId>me.yushust.message</groupId>
    <artifactId>sourcetype-gson-json</artifactId>
    <version>7.1.0</version>
</dependency>

<!-- Bukkit YAML source type and helpers -->
<dependency>
    <groupId>me.yushust.message</groupId>
    <artifactId>sourcetype-bukkit-yml</artifactId>
    <version>7.1.0</version>
</dependency>

<!-- BungeeCord YAML source type and helpers -->
<dependency>
    <groupId>me.yushust.message</groupId>
    <artifactId>sourcetype-bungee-yml</artifactId>
    <version>7.1.0</version>
</dependency>
```
