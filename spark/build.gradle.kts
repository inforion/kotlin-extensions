dependencies {
    implementation(project(":extensions"))
    implementation(project(":kafka"))
    implementation(project(":concurrent"))

    implementation("org.jetbrains.kotlinx.spark:core-3.0_2.12:1.0.1")
    implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api-3.0:1.0.1")
    implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api-common:1.0.1")
    implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api-parent_2.12:1.0.1")

    implementation("org.apache.spark:spark-sql_2.12:3.1.2")
    implementation("org.apache.spark:spark-core_2.12:3.1.2")
    implementation("org.apache.spark:spark-streaming_2.12:3.1.2")
    implementation("org.apache.spark:spark-streaming-kafka-0-10_2.12:3.1.2")
    implementation("org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2")
}