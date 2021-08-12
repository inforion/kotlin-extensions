val jodaTimeVersion: String by project
val moshiVersion: String by project

dependencies {
    implementation(project(":extensions"))

    api("com.squareup.moshi:moshi:$moshiVersion")
//    api("joda-time:joda-time:$jodaTimeVersion")
}