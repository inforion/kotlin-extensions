val unirestJavaVersion: String by project
val kotlinJacksonVersion: String by project
val jacksonVersion: String by project

dependencies {
    implementation(project(":json"))
    implementation(project(":logging"))
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.konghq:unirest-java:$unirestJavaVersion")
}