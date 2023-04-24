plugins {
    id("message.java-conventions")
}

dependencies {
    implementation(project(":core"))
    implementation("org.spongepowered:configurate-yaml:4.0.0")
}