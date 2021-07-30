val kafkaClientsVersion: String by project
val jodaTimeVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":gson-json"))
    implementation(project(":logging"))

    api("joda-time:joda-time:$jodaTimeVersion")
    api("org.apache.kafka:kafka-clients:$kafkaClientsVersion")
}