val javalinVersion: String by project
val jacksonVersion: String by project
val jodaTimeVersion: String by project

dependencies {
    implementation(project(":json"))

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:$jacksonVersion")

    implementation("joda-time:joda-time:$jodaTimeVersion")

    implementation("io.javalin:javalin:$javalinVersion") { isTransitive = false }
}