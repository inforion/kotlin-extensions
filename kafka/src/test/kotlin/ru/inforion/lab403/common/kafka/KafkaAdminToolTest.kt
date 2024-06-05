package ru.inforion.lab403.common.kafka

import org.junit.jupiter.api.Test

class KafkaAdminToolTest {

    companion object {
        private val admin = KafkaAdminTool("localhost:9092", 10000)
    }

    @Test
    internal fun resetOffset() {
        admin.resetOffsets("test_topic1-consumer-group", Topic("Test_topic1", 21, 1, 1073741824, 134217728, "delete"))
    }

    @Test
    internal fun checkTopics() {
        println(
            admin.checkAllDataConsumed(listOf(
                "test_topic1-consumer-group",
                "test_topic2-consumer-group"
            ))
        )
    }
}