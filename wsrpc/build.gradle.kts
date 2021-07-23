val javaWebSocketVersion: String by project
val jacksonVersion: String by project

dependencies {
    implementation(project(":logging"))
    implementation(project(":extensions"))
    implementation(project(":concurrent"))
    implementation(project(":scripts"))
    implementation(project(":uuid"))
    implementation(project(":json"))

    implementation("org.java-websocket:Java-WebSocket:$javaWebSocketVersion")

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion") {
        isTransitive = false
    }
}