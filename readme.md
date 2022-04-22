# nmessage ![Build Status](https://img.shields.io/github/workflow/status/yusshu/nmessage/build/master) [![MIT License](https://img.shields.io/badge/license-MIT-blue)](license.txt)

A simple and a bit abstract library to handle messages with multilanguage support.
I have seen many things in languages ​​that I do not understand, to avoid this, I bring here a library for multilanguage and easy to obtain messages from configuration files or from any other place

## Wiki
Read the wiki [here](https://github.com/yusshu/nmessage/wiki)

## Installation

Latest snapshot: [![Latest Snapshot](https://img.shields.io/nexus/s/me.yushust.message/core.svg?server=https%3A%2F%2Frepo.unnamed.team)](https://repo.unnamed.team/repository/unnamed-snapshots)

Latest release: [![Latest Release](https://img.shields.io/nexus/r/me.yushust.message/core.svg?server=https%3A%2F%2Frepo.unnamed.team)](https://repo.unnamed.team/repository/unnamed-releases/)

### Gradle
**For Groovy DSL:** (build.gradle)
Repository:
```groovy
maven {
  name = 'unnamed-public'
  url = 'https://repo.unnamed.team/repository/unnamed-public/'
}
```
Dependency:
```groovy
implementation 'me.yushust.message:core:VERSION'
```

**For Kotlin DSL:** (build.gradle.kts)
Repository:
```kotlin
maven {
  name = "unnamed-public"
  url = uri("https://repo.unnamed.team/repository/unnamed-public/")
}
```
Dependency:
```kotlin
implementation("me.yushust.message:core:VERSION")
```

### Maven
Add this repository into your `<repositories>` tag (`pom.xml`)
```xml
<repository>
  <id>unnamed-public</id>
  <url>https://repo.unnamed.team/repository/unnamed-public/</url>
</repository>
```
Add the dependency into your `<dependencies>` tag (`pom.xml`)
```xml
<dependency>
  <groupId>me.yushust.message</groupId>
  <artifactId>core</artifactId>
  <version>VERSION</version>
</dependency>
```
