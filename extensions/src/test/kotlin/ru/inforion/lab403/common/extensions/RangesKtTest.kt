package ru.inforion.lab403.common.extensions

import kotlin.math.max
import kotlin.math.min
import org.junit.Test
import kotlin.test.assertEquals

internal class RangesKtTest {

    @Test fun ulong_intersect_10_15() = assertEquals(10uL..15uL, 10uL..50uL intersect 5uL..15uL)

    @Test fun ulong_intersect_0xFFFF_FFFF_0000_0000() =
        assertEquals(0xFFFF_FFFF_0000_0000uL..0xFFFF_FFFF_0000_FFFFuL,
            0xFFFF_FFFF_0000_0000uL..0xFFFF_FFFF_0000_FFFFuL intersect 0xFFFF_0000_0000_0000uL..0xFFFF_FFFF_FFFF_0000uL)

    @Test fun ulong_isIntersect_0xFFFF_FFFF_0000_0000_true() =
        assertEquals(true, 0xFFFF_FFFF_0000_0000uL..0xFFFF_FFFF_0000_FFFFuL isIntersect  0xFFFF_0000_0000_0000uL..0xFFFF_FFFF_FFFF_0000uL)

    @Test fun ulong_isIntersect_0xFFFF_FFFF_0000_0000_false() =
        assertEquals(false, 0xFFFF_FFFF_0000_0000uL..0xFFFF_FFFF_0000_FFFFuL isIntersect  0xFFFF_FFFF_FFFF_0000uL..0xFFFF_FFFF_FFFF_FFFFuL)

    @Test fun ulong_isIntersect_edge1_true() =
        assertEquals(true, 1uL..5uL isIntersect 5uL..7uL)

    @Test fun ulong_isIntersect_edge2_true() =
        assertEquals(true, 0xFFFF_FFFF_0000_0000uL..0xFFFF_FFFF_0000_0005uL isIntersect 0xFFFF_FFFF_0000_0005uL..0xFFFF_FFFF_0000_0007uL)

    @Test fun ulong_isIntersect_edge3_false() =
        assertEquals(false, 0xFFFF_FFFF_0000_0000uL..0xFFFF_FFFF_0000_0004uL isIntersect 0xFFFF_FFFF_0000_0005uL..0xFFFF_FFFF_0000_0007uL)

    @Test fun ulong_isIntersect_edge4_false() =
        assertEquals(false, 0xFFFF_FFFF_0000_0000uL..0xFFFF_FFFF_0000_0005uL isIntersect 0xFFFF_FFFF_0000_0006uL..0xFFFF_FFFF_0000_0007uL)
}