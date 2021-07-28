val jacksonVersion: String by project
val javalinVersion: String by project

dependencies {
//    implementation(project(":json"))

    api("io.javalin:javalin:$javalinVersion") { isTransitive = false }
    api("com.fasterxml.jackson.datatype:jackson-datatype-joda:$jacksonVersion")
}