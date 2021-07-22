val kafkaClientsVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":json"))
    implementation(project(":logging"))
    implementation("org.apache.kafka:kafka-clients:$kafkaClientsVersion")
}