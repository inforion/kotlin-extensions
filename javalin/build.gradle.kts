val javalinVersion: String by project

dependencies {
    implementation(project(":json"))

    api("io.javalin:javalin:$javalinVersion") { isTransitive = false }
}