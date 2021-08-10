val jodaTimeVersion: String by project
val gsonVersion: String by project

dependencies {
    implementation(project(":extensions"))

    api("com.google.code.gson:gson:$gsonVersion")
}