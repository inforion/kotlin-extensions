val systemLambdaVersion: String by project
val jacksonVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":json"))

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion") {
        isTransitive = false
    }

    testImplementation("com.github.stefanbirkner:system-lambda:$systemLambdaVersion")
}