val jodaTimeVersion: String by project
val jacksonVersion: String by project

dependencies {
    api("joda-time:joda-time:$jodaTimeVersion")

    api("com.fasterxml.jackson.datatype:jackson-datatype-joda:$jacksonVersion")
    api("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    api("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    api("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion") {
        isTransitive = false
    }
}