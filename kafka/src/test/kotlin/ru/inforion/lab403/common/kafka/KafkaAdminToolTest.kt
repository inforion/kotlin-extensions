package ru.inforion.lab403.common.kafka

import org.junit.Test

class KafkaAdminToolTest {
    @Test
    internal fun resetOffset() {
        val admin = KafkaAdminTool("localhost:9092", 10000)
        admin.resetOffsets("traces-consumer-group", "Traces", emptyMap(), 8)
    }
}