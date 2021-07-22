package ru.inforion.lab403.common.extensions

import unsigned.types.*
import unsigned.literal.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RangesKtTest {

    @Test fun ulong_intersect_10_15() = assertEquals(
        ul[10]..ul[15], ul[10]..ul[50] intersect ul[5]..ul[15])

    @Test fun ulong_intersect_0xFFFF_FFFF_0000_0000() =
        assertEquals(
            ul[0xFFFF_FFFF_0000_0000u]..ul[0xFFFF_FFFF_0000_FFFFu],
            ul[0xFFFF_FFFF_0000_0000u]..ul[0xFFFF_FFFF_0000_FFFFu] intersect
                    ul[0xFFFF_0000_0000_0000u]..ul[0xFFFF_FFFF_FFFF_0000u])

    @Test fun ulong_isIntersect_0xFFFF_FFFF_0000_0000_true() =
        assertTrue(ul[0xFFFF_FFFF_0000_0000u]..ul[0xFFFF_FFFF_0000_FFFFu] isIntersect
                ul[0xFFFF_0000_0000_0000u]..ul[0xFFFF_FFFF_FFFF_0000u])

    @Test fun ulong_isIntersect_0xFFFF_FFFF_0000_0000_false() =
        assertFalse(ul[0xFFFF_FFFF_0000_0000u]..ul[0xFFFF_FFFF_0000_FFFFu] isIntersect
                ul[0xFFFF_FFFF_FFFF_0000u]..ul[0xFFFF_FFFF_FFFF_FFFFu])

    @Test fun ulong_isIntersect_edge1_true() =
        assertTrue(ul[1]..ul[5] isIntersect ul[5]..ul[7])

    @Test fun ulong_isIntersect_edge2_true() =
        assertTrue(ul[0xFFFF_FFFF_0000_0000u]..ul[0xFFFF_FFFF_0000_0005u] isIntersect
                ul[0xFFFF_FFFF_0000_0005u]..ul[0xFFFF_FFFF_0000_0007u])

    @Test fun ulong_isIntersect_edge3_false() =
        assertFalse(ul[0xFFFF_FFFF_0000_0000u]..ul[0xFFFF_FFFF_0000_0004u] isIntersect
                ul[0xFFFF_FFFF_0000_0005u]..ul[0xFFFF_FFFF_0000_0007u])

    @Test fun ulong_isIntersect_edge4_false() =
        assertFalse(ul[0xFFFF_FFFF_0000_0000u]..ul[0xFFFF_FFFF_0000_0005u] isIntersect
                ul[0xFFFF_FFFF_0000_0006u]..ul[0xFFFF_FFFF_0000_0007u])
}