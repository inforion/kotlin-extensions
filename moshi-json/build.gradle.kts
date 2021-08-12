val jodaTimeVersion: String by project
val moshiVersion: String by project

dependencies {
    implementation(project(":extensions"))

    api("com.squareup.moshi:moshi:$moshiVersion")
    api("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    api("com.squareup.moshi:moshi-adapters:$moshiVersion")
}