package ru.inforion.lab403.common.extensions

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
        assertEquals(true, 0xFFFF_FFFF_0000_0000uL..0xFFFF_FFFF_0000_FFFFuL isIntersect  0xFFFF_FFFF_FFFF_0000uL..0xFFFF_FFFF_FFFF_FFFFuL)
}