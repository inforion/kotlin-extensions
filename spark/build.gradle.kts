val kotlinSparkVersion: String by project
val sparkVersion: String by project
val scalaVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":kafka"))
    implementation(project(":concurrent"))

    testImplementation(project(":kryo"))

    api("org.jetbrains.kotlinx.spark:core-3.0_$scalaVersion:$kotlinSparkVersion")
    api("org.jetbrains.kotlinx.spark:kotlin-spark-api-3.0:$kotlinSparkVersion")

    api("org.apache.spark:spark-streaming_$scalaVersion:$sparkVersion")
    api("org.apache.spark:spark-streaming-kafka-0-10_$scalaVersion:$sparkVersion")
}