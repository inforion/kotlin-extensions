val jblasVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation("org.jblas:jblas:$jblasVersion")
}