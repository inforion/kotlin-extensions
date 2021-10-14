package ru.inforion.lab403.common.kafka

data class Topic(
    val name: String,
    val partitions: Int,
    val replication: Short = 1,
    val retention: Long? = null,
    val segment: Long? = null,
    val policy: String? = null
)