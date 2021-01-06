[releaseImg]: https://img.shields.io/github/v/release/yusshu/nmessage.svg?label=github%20release
[release]: https://github.com/yusshu/nmessage/releases/latest

# NMessage [![releaseImg]][release] [![Build Status](https://travis-ci.com/yusshu/nmessage.svg?branch=master)](https://travis-ci.com/yusshu/nmessage) 

A simple and a bit abstract library to handle messages with multilanguage support.
I have seen many things in languages ​​that I do not understand, to avoid this, I bring here a library for multilanguage and easy to obtain messages from configuration files or from any other place

## Wiki
Read the wiki [here](https://github.com/yusshu/nmessage/wiki)

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
  <artifactId>message-dispatch-core</artifactId>
  <version>VERSION</version>
</dependency>
```

Bukkit adapters maven dependency
(Adds support for YML using Bukkit's `YamlConfiguration`)
```xml
<dependency>
  <groupId>me.yushust.message</groupId>
  <artifactId>bukkit-message-dispatch</artifactId>
  <version>VERSION</version>
</dependency>
```
> "Yes."
