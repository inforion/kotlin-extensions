val systemLambdaVersion: String by project
val jacksonVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":json"))

    testImplementation("com.github.stefanbirkner:system-lambda:$systemLambdaVersion")
}