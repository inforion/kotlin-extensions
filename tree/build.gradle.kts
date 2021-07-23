val jacksonVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    testImplementation(project(":json"))
}