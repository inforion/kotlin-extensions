val jythonStandaloneVersion: String by project

dependencies {
    implementation("org.python:jython-standalone:$jythonStandaloneVersion")

    implementation(project(":extensions"))
    implementation(kotlin("scripting-jsr223"))
    implementation(kotlin("scripting-jvm-host"))
    implementation(kotlin("scripting-compiler-embeddable"))
    compileOnly(kotlin("compiler-embeddable"))
}
