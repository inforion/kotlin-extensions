val mongoJavaDriverVersion: String by project
val mongodbDriverVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":identifier"))

    api("org.mongodb:mongo-java-driver:$mongoJavaDriverVersion")
    api("org.mongodb:mongodb-driver:$mongodbDriverVersion")
}