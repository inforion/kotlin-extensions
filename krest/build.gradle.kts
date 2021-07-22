val unirestJavaVersion: String by project
val kotlinJacksonVersion: String by project
val jacksonCoreVersion: String by project

dependencies {
    implementation(project(":json"))
    implementation(project(":logging"))
    implementation("com.konghq:unirest-java:$unirestJavaVersion")
}