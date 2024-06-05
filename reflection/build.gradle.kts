val reflectionsVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(kotlin("reflect"))
}