val kafkaClientsVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":gson-json"))
    implementation(project(":logging"))

    api("org.apache.kafka:kafka-clients:$kafkaClientsVersion")
}