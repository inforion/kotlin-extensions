val jythonStandaloneVersion: String by project

dependencies {
    implementation("org.python:jython-standalone:$jythonStandaloneVersion")

    implementation(project(":extensions"))
    implementation(kotlin("script-runtime"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("script-util"))
    implementation(kotlin("scripting-compiler-embeddable"))
}