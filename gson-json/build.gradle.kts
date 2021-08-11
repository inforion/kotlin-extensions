val jodaTimeVersion: String by project
val gsonVersion: String by project
val gsonExtrasVersion: String by project

dependencies {
    api("com.google.code.gson:gson:$gsonVersion")
    api("org.danilopianini:gson-extras:$gsonExtrasVersion")
}