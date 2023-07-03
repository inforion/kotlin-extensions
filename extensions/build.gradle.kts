val jmhCoreVersion: String by project

plugins {
    id("me.champeau.jmh") version "0.6.5"
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