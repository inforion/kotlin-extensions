package ru.inforion.lab403.common.kafka

data class PartitionInfo(val id: Int, val offset: Long, val size: Long)