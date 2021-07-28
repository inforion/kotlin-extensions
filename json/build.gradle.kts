val jodaTimeVersion: String by project
val kotlinxSerializationVersion: String by project

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    api("joda-time:joda-time:$jodaTimeVersion")
}