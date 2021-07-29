val unirestJavaVersion: String by project

dependencies {
    implementation(project(":gson-json"))
    implementation(project(":logging"))

    api("com.konghq:unirest-java:$unirestJavaVersion")
}