plugins {
    id("java")
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":core"))
    implementation("org.spongepowered:configurate-yaml:4.0.0")
}