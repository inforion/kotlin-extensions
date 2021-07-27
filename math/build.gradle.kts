val jblasVersion: String by project

dependencies {
    implementation(project(":extensions"))

    api("org.jblas:jblas:$jblasVersion")
}