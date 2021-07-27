val bsonVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":serialization"))

    api("org.mongodb:bson:$bsonVersion")
}