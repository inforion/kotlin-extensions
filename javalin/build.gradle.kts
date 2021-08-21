val jacksonVersion: String by project
val javalinVersion: String by project

dependencies {
    implementation(project(":gson-json"))

    api("io.javalin:javalin:$javalinVersion")
}