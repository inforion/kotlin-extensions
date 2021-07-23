val kafkaClientsVersion: String by project
val jacksonVersion: String by project
val jodaTimeVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":json"))
    implementation(project(":logging"))
    implementation("org.apache.kafka:kafka-clients:$kafkaClientsVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("joda-time:joda-time:$jodaTimeVersion")
}