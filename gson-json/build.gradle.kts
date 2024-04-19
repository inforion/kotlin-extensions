val jodaTimeVersion: String by project
val gsonVersion: String by project
val gsonExtrasVersion: String by project
val oshiVersion: String by project

dependencies {
    implementation(project(":optional"))
    implementation(project(":extensions"))
    implementation(project(":reflection"))
    implementation(project(":identifier"))

    api("com.google.code.gson:gson:$gsonVersion")
    api("org.danilopianini:gson-extras:$gsonExtrasVersion")
    api("joda-time:joda-time:$jodaTimeVersion")

    testImplementation(group="com.github.oshi", name="oshi-core", version=oshiVersion)
}