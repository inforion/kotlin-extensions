package ru.inforion.lab403.common.extensions

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RangesKtTest {

    @Test fun ulong_intersect_10_15() = assertEquals(
        10uL..15uL, 10uL..50uL intersect 5uL..15uL)

    @Test fun ulong_intersect_0xFFFF_FFFF_0000_0000() =
        assertEquals(
            0xFFFF_FFFF_0000_0000u..0xFFFF_FFFF_0000_FFFFu,
            0xFFFF_FFFF_0000_0000u..0xFFFF_FFFF_0000_FFFFu intersect
                    0xFFFF_0000_0000_0000u..0xFFFF_FFFF_FFFF_0000u)

    @Test fun ulong_isIntersect_0xFFFF_FFFF_0000_0000_true() =
        assertTrue(0xFFFF_FFFF_0000_0000u..0xFFFF_FFFF_0000_FFFFu isIntersect
                0xFFFF_0000_0000_0000u..0xFFFF_FFFF_FFFF_0000u)

    @Test fun ulong_isIntersect_0xFFFF_FFFF_0000_0000_false() =
        assertFalse(0xFFFF_FFFF_0000_0000u..0xFFFF_FFFF_0000_FFFFu isIntersect
                0xFFFF_FFFF_FFFF_0000u..0xFFFF_FFFF_FFFF_FFFFu)

    @Test fun ulong_isIntersect_edge1_true() =
        assertTrue(1uL..5uL isIntersect 5uL..7uL)

    @Test fun ulong_isIntersect_edge2_true() =
        assertTrue(0xFFFF_FFFF_0000_0000u..0xFFFF_FFFF_0000_0005u isIntersect
                0xFFFF_FFFF_0000_0005u..0xFFFF_FFFF_0000_0007u)

    @Test fun ulong_isIntersect_edge3_false() =
        assertFalse(0xFFFF_FFFF_0000_0000u..0xFFFF_FFFF_0000_0004u isIntersect
                0xFFFF_FFFF_0000_0005u..0xFFFF_FFFF_0000_0007u)

    @Test fun ulong_isIntersect_edge4_false() =
        assertFalse(0xFFFF_FFFF_0000_0000u..0xFFFF_FFFF_0000_0005u isIntersect
                0xFFFF_FFFF_0000_0006u..0xFFFF_FFFF_0000_0007u)
}