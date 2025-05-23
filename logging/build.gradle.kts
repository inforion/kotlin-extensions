plugins {
    id("me.champeau.jmh") version "0.6.5"
}

val systemLambdaVersion: String by project
val jacksonVersion: String by project
val slf4jVersion: String by project

dependencies {
    implementation(project(":gson-json"))
    implementation(project(":extensions"))
    implementation(kotlin("reflect"))
    api("org.slf4j:slf4j-api:$slf4jVersion")

    testImplementation("com.github.stefanbirkner:system-lambda:$systemLambdaVersion")
    jmh("commons-io:commons-io:2.7")
    jmh("org.openjdk.jmh:jmh-core:0.9")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:0.9")
    jmh("org.openjdk.jmh:jmh-generator-bytecode:0.9")
}

tasks {
    compileJmhKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

jmh {
//    includes.add("LoggerNoCacheTest")
    includes.add("CacheBenchMarkTest")
//    verbosity.set("SILENT")
}