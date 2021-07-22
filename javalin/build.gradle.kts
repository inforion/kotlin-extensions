val javalinVersion: String by project

dependencies {
    implementation(project(":json"))
    implementation("io.javalin:javalin:$javalinVersion") {
        isTransitive = false
    }
}