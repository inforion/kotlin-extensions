package ru.inforion.lab403.common.extensions.kafka

data class TopicInfo(val name: String, val partitions: List<PartitionInfo>)
