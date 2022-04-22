plugins {
    id("message.java-conventions")
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:20.1.0")
    testImplementation(project(":sourcetype-properties"))
}
