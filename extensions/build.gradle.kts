val jmhCoreVersion: String by project

plugins {
    id("me.champeau.jmh") version "0.7.2"
}

dependencies {
    jmh("org.openjdk.jmh:jmh-core:$jmhCoreVersion")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:$jmhCoreVersion")

    testApi(project(":utils"))
}

tasks {
    compileJmhKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}