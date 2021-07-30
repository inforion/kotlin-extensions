val reflectionsVersion: String by project

dependencies {
    implementation(project(":extensions"))

    api("org.reflections:reflections:$reflectionsVersion")
}