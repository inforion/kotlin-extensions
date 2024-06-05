val kryoVersion: String by project
val twitterChillVersion: String by project
val scalaVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":logging"))
    implementation(project(":jodatime"))

    api("com.esotericsoftware:kryo:$kryoVersion")
    api("com.twitter:chill_$scalaVersion:$twitterChillVersion")
}