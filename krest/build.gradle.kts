val unirestJavaVersion: String by project

dependencies {
    implementation(project(":json"))
    implementation(project(":logging"))

    api("com.konghq:unirest-java:$unirestJavaVersion")
}