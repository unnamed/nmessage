plugins {
  id("message.java-conventions")
}

repositories {
  maven("https://oss.sonatype.org/content/repositories/snapshots/")
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
  api(project(":core"))
  compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}
