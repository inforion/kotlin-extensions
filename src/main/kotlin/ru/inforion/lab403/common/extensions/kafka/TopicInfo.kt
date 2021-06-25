package ru.inforion.lab403.common.extensions.kafka

import ru.inforion.lab403.common.extensions.kafka.PartitionInfo

data class TopicInfo(val name: String, val partitions: List<PartitionInfo>)
