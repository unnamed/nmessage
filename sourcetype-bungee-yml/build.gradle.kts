plugins {
    id("message.java-conventions")
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    api(project(":core"))
    compileOnly("net.md-5:bungeecord-api:1.18-R0.1-SNAPSHOT")
}