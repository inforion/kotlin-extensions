val systemLambdaVersion: String by project
val jacksonVersion: String by project

dependencies {
    implementation(project(":gson-json"))
    implementation(project(":extensions"))

    testImplementation("com.github.stefanbirkner:system-lambda:$systemLambdaVersion")
}