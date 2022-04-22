plugins {
  id("message.java-conventions")
}

dependencies {
  api(project(":core"))
  testImplementation(project(":core"))
}