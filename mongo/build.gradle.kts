val mongoJavaDriverVersion: String by project
val mongodbDriverVersion: String by project

dependencies {
    implementation(project(":unsigned"))
    implementation(project(":extensions"))
    implementation(project(":identifier"))
    implementation("org.mongodb:mongo-java-driver:$mongoJavaDriverVersion")
    implementation("org.mongodb:mongodb-driver:$mongodbDriverVersion")
}