val kotlinSparkVersion: String by project
val sparkVersion: String by project
val scalaVersion: String by project

dependencies {
    implementation(project(":extensions"))
    implementation(project(":kafka"))
    implementation(project(":concurrent"))

    implementation("org.jetbrains.kotlinx.spark:core-3.0_$scalaVersion:$kotlinSparkVersion")
    implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api-3.0:$kotlinSparkVersion")
    implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api-common:$kotlinSparkVersion")
    implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api-parent_$scalaVersion:$kotlinSparkVersion")

//    implementation("org.apache.spark:spark-sql_$scalaVersion:$sparkVersion")
    implementation("org.apache.spark:spark-core_$scalaVersion:$sparkVersion")
    implementation("org.apache.spark:spark-streaming_$scalaVersion:$sparkVersion")
    implementation("org.apache.spark:spark-streaming-kafka-0-10_$scalaVersion:$sparkVersion")
//    implementation("org.apache.spark:spark-sql-kafka-0-10_$scalaVersion:$sparkVersion")
}