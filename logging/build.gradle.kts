val systemLambdaVersion: String by project
val jacksonVersion: String by project

dependencies {
    implementation(project(":gson-json"))
    implementation(project(":extensions"))
    api("org.slf4j:slf4j-api:2.0.6")

    testImplementation("com.github.stefanbirkner:system-lambda:$systemLambdaVersion")
}