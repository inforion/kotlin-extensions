val javaWebSocketVersion: String by project

dependencies {
    implementation(project(":logging"))
    implementation(project(":extensions"))
    implementation(project(":concurrent"))
    implementation(project(":scripts"))
    implementation(project(":uuid"))
    implementation(project(":json"))
    implementation("org.java-websocket:Java-WebSocket:$javaWebSocketVersion")
}