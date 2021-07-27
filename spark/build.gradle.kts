val kotlinSparkVersion: String by project
val sparkVersion: String by project
val scalaVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":kafka"))
    implementation(project(":concurrent"))

    api("org.jetbrains.kotlinx.spark:core-3.0_$scalaVersion:$kotlinSparkVersion")
    api("org.jetbrains.kotlinx.spark:kotlin-spark-api-3.0:$kotlinSparkVersion")
    api("org.jetbrains.kotlinx.spark:kotlin-spark-api-common:$kotlinSparkVersion")
    api("org.jetbrains.kotlinx.spark:kotlin-spark-api-parent_$scalaVersion:$kotlinSparkVersion")

    api("org.apache.spark:spark-core_$scalaVersion:$sparkVersion")
    api("org.apache.spark:spark-streaming_$scalaVersion:$sparkVersion")
    api("org.apache.spark:spark-streaming-kafka-0-10_$scalaVersion:$sparkVersion")
}