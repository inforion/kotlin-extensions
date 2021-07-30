val javaWebSocketVersion: String by project

dependencies {
    implementation(project(":logging"))
    implementation(project(":extensions"))
    implementation(project(":concurrent"))
    implementation(project(":reflection"))
    implementation(project(":scripts"))
    implementation(project(":uuid"))
    implementation(project(":gson-json"))

    api("org.java-websocket:Java-WebSocket:$javaWebSocketVersion")
}