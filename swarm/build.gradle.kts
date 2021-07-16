dependencies {
    implementation(files("lib/mpi.jar"))

    implementation(project(":logging"))
    implementation(project(":concurrent"))
    implementation(project(":serialization"))
    implementation(project(":extensions"))
}