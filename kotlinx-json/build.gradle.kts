plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

val jodaTimeVersion: String by project
val kotlinxSerializationVersion: String by project

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation(kotlin("reflect"))
}