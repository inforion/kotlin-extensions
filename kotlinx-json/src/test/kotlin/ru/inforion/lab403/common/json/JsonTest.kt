package ru.inforion.lab403.common.json

import kotlinx.serialization.Serializable
import org.junit.Test
import kotlin.test.assertEquals

internal class JsonTest {

    @Serializable
    data class Testik(val data: UInt)

    @Test
    fun unsignedTest() {
        val expected = Testik(0xFFFF_FFFFu)
        val json = expected.toJson()

        println(json)

        val actual = json.fromJson<Testik>()

        println(actual)
        assertEquals(expected, actual)
    }
}