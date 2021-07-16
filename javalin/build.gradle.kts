dependencies {
    implementation(project(":json"))
    implementation("io.javalin:javalin:3.13.9") {
        isTransitive = false
    }
}