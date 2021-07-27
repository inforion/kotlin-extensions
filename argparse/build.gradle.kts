val argparse4jVersion: String by project

dependencies {
    implementation(project(":extensions"))

    api("net.sourceforge.argparse4j:argparse4j:$argparse4jVersion")
}