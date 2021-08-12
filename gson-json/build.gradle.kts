val jodaTimeVersion: String by project
val gsonVersion: String by project
val gsonExtrasVersion: String by project

dependencies {
    implementation(project(":extensions"))

    api("com.google.code.gson:gson:$gsonVersion")
    api("org.danilopianini:gson-extras:$gsonExtrasVersion")
    api("joda-time:joda-time:$jodaTimeVersion")
}