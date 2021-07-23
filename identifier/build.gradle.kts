val bsonVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":serialization"))

    implementation("org.mongodb:bson:$bsonVersion")
}