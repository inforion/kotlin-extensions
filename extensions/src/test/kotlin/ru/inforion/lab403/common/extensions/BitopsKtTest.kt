package ru.inforion.lab403.common.extensions

import org.junit.Test
import ru.inforion.lab403.common.utils.Benchmark
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class BitopsKtTest {
    @Test
    fun long_ashr_31() = assertEquals(-3414779717, 0x9A3B4C5DDEADBEEFu.long ashr 31)
    @Test
    fun int_ashr_16() = assertEquals(-8531, 0xDEADBEEFu.int ashr 16)
    @Test
    fun short_ashr_8() = assertEquals(-66, 0xBEEFu.short ashr 8)
    @Test
    fun byte_ashr_4() = assertEquals(-2, 0xEFu.byte ashr 4)

    @Test
    fun long_ushr_31() = assertEquals(5175154875, 0x9A3B4C5DDEADBEEFu.long ushr 31)
    @Test
    fun int_ushr_16() = assertEquals(57005, 0xDEADBEEFu.int ushr 16)
    @Test
    fun short_ushr_8() = assertEquals(190, 0xBEEFu.short ushr 8)
    @Test
    fun byte_ushr_4() = assertEquals(14, 0xEFu.byte ushr 4)

    @Test
    fun ulong_ashr_32() = assertEquals(0xFFFFFFFF_9A3B4C5Du, 0x9A3B4C5DDEADBEEFu ashr 32)
    @Test
    fun uint_ashr_16() = assertEquals(0xFFFF_DEADu, 0xDEADBEEFu ashr 16)
    @Test
    fun ushort_ashr_8() = assertEquals(0xFFBEu, 0xBEEFu.ushort ashr 8)
    @Test
    fun ubyte_ashr_4() = assertEquals(0xFEu, 0xEFu.ubyte ashr 4)

    @Test
    fun ulong_ushr_32() = assertEquals(0x9A3B4C5DuL, 0x9A3B4C5DDEADBEEFu ushr 32)
    @Test
    fun uint_ushr_16() = assertEquals(0xDEADu, 0xDEADBEEFu ushr 16)
    @Test
    fun ushort_ushr_8() = assertEquals(0xBEu, 0xBEEFu.ushort ushr 8)
    @Test
    fun ubyte_ushr_4() = assertEquals(0xEu, 0xEFu.ubyte ushr 4)

    @Test
    fun int_bitrev32() = assertEquals(0x195D3B7F, 0xFEDCBA98.int.bitrev32())
    @Test
    fun long_bitrev32() = assertEquals(0x195D3B7F, 0xFEDCBA98L.bitrev32())
    @Test
    fun long_bitrev64() = assertEquals(0xF7B3D591E6A2C480u.long, 0x123456789ABCDEF.bitrev64())

    @Test
    fun bitMask64_1() = assertEquals(0x01, bitMask64(1))
    @Test
    fun bitMask64_2() = assertEquals(0x03, bitMask64(2))
    @Test
    fun bitMask64_3() = assertEquals(0x07, bitMask64(3))
    @Test
    fun bitMask64_4() = assertEquals(0x0F, bitMask64(4))
    @Test
    fun bitMask64_5() = assertEquals(0x1F, bitMask64(5))
    @Test
    fun bitMask64_6() = assertEquals(0x3F, bitMask64(6))
    @Test
    fun bitMask64_7() = assertEquals(0x7F, bitMask64(7))

    @Test
    fun bitMask64_15() = assertEquals(0x0000_7FFF, bitMask64(15))
    @Test
    fun bitMask64_19() = assertEquals(0x0007_FFFF, bitMask64(19))
    @Test
    fun bitMask64_23() = assertEquals(0x007F_FFFF, bitMask64(23))
    @Test
    fun bitMask64_27() = assertEquals(0x07FF_FFFF, bitMask64(27))
    @Test
    fun bitMask64_31() = assertEquals(0x7FFF_FFFF, bitMask64(31))
    @Test
    fun bitMask64_32() = assertEquals(0xFFFF_FFFF, bitMask64(32))

    @Test
    fun bitMask64_64() = assertEquals(-1, bitMask64(64))

    @Test
    fun bitMask64_0() {
        assertFails { bitMask64(0) }
    }

    @Test
    fun bitMask64_65() {
        assertFails { bitMask64(65) }
    }

    @Test
    fun bitMask64_07_04() = assertEquals(0x0000_00F0, bitMask64(7..4))
    @Test
    fun bitMask64_11_08() = assertEquals(0x0000_0F00, bitMask64(11..8))
    @Test
    fun bitMask64_15_12() = assertEquals(0x0000_F000, bitMask64(15..12))
    @Test
    fun bitMask64_19_16() = assertEquals(0x000F_0000, bitMask64(19..16))
    @Test
    fun bitMask64_23_20() = assertEquals(0x00F0_0000, bitMask64(23..20))
    @Test
    fun bitMask64_27_24() = assertEquals(0x0F00_0000, bitMask64(27..24))
    @Test
    fun bitMask64_31_28() = assertEquals(0xF000_0000, bitMask64(31..28))

    @Test
    fun bitMask64_3_0() = assertEquals(0x0000_000F, bitMask64(3..0))
    @Test
    fun bitMask64_7_0() = assertEquals(0x0000_00FF, bitMask64(7..0))
    @Test
    fun bitMask64_11_0() = assertEquals(0x0000_0FFF, bitMask64(11..0))
    @Test
    fun bitMask64_15_0() = assertEquals(0x0000_FFFF, bitMask64(15..0))
    @Test
    fun bitMask64_19_0() = assertEquals(0x000F_FFFF, bitMask64(19..0))
    @Test
    fun bitMask64_23_0() = assertEquals(0x00FF_FFFF, bitMask64(23..0))
    @Test
    fun bitMask64_27_0() = assertEquals(0x0FFF_FFFF, bitMask64(27..0))
    @Test
    fun bitMask64_31_0() = assertEquals(0xFFFF_FFFF, bitMask64(31..0))

    @Test
    fun ubitMask64_31_0() = assertEquals(0x00000000_FFFFFFFFuL, ubitMask64(31..0))
    @Test
    fun ubitMask64_35_0() = assertEquals(0x0000000F_FFFFFFFFuL, ubitMask64(35..0))
    @Test
    fun ubitMask64_39_0() = assertEquals(0x000000FF_FFFFFFFFuL, ubitMask64(39..0))
    @Test
    fun ubitMask64_43_0() = assertEquals(0x00000FFF_FFFFFFFFuL, ubitMask64(43..0))
    @Test
    fun ubitMask64_47_0() = assertEquals(0x0000FFFF_FFFFFFFFuL, ubitMask64(47..0))
    @Test
    fun ubitMask64_51_0() = assertEquals(0x000FFFFF_FFFFFFFFuL, ubitMask64(51..0))
    @Test
    fun ubitMask64_55_0() = assertEquals(0x00FFFFFF_FFFFFFFFuL, ubitMask64(55..0))
    @Test
    fun ubitMask64_59_0() = assertEquals(0x0FFFFFFF_FFFFFFFFuL, ubitMask64(59..0))
    @Test
    fun ubitMask64_63_0() = assertEquals(0xFFFFFFFF_FFFFFFFFuL, ubitMask64(63..0))

    @Test
    fun mask_7_4() = assertEquals(0xA0, 0xAD mask 7..4)
    @Test
    fun mask_7_0() = assertEquals(0xAD, 0xAD mask 7..0)
    @Test
    fun mask_6_5() = assertEquals(0x20, 0xAD mask 6..5)
    @Test
    fun mask_4_3() = assertEquals(0x08, 0xAD mask 4..3)
    @Test
    fun mask_5_2() = assertEquals(0x2C, 0xAD mask 5..2)
    @Test
    fun mask_15_0() = assertEquals(0xAD, 0xAD mask 15..0)
    @Test
    fun mask_63_14() = assertEquals(0x00, 0xADL mask 63..14)
    @Test
    fun mask_3_2() = assertEquals(0x0C, 0xAD mask 3..2)
    @Test
    fun mask_0_0() = assertEquals(0x01, 0xAD mask 0..0)

    @Test
    fun long_bzero_59_4() = assertEquals(-1152921504606846721, -1L bzero 59..8)
    @Test
    fun int_bzero_27_4() = assertEquals(-268435441, -1 bzero 27..4)
    @Test
    fun short_bzero_13_2() = assertEquals(-16381, (-1).short bzero 13..2)
    @Test
    fun byte_bzero_6_4() = assertEquals(-113, (-1).byte bzero 6..4)

    private val longV = 0x9A3B4C5DDEADBEEFu.long

    @Test
    fun get_3_0() = assertEquals(0xF, longV[3..0])
    @Test
    fun get_7_4() = assertEquals(0xE, longV[7..4])
    @Test
    fun get_11_8() = assertEquals(0xE, longV[11..8])
    @Test
    fun get_15_12() = assertEquals(0xB, longV[15..12])
    @Test
    fun get_19_16() = assertEquals(0xD, longV[19..16])
    @Test
    fun get_23_20() = assertEquals(0xA, longV[23..20])
    @Test
    fun get_27_24() = assertEquals(0xE, longV[27..24])
    @Test
    fun get_31_28() = assertEquals(0xD, longV[31..28])

    @Test
    fun get_35_32() = assertEquals(0xD, longV[35..32])
    @Test
    fun get_39_36() = assertEquals(0x5, longV[39..36])
    @Test
    fun get_43_40() = assertEquals(0xC, longV[43..40])
    @Test
    fun get_47_44() = assertEquals(0x4, longV[47..44])
    @Test
    fun get_51_48() = assertEquals(0xB, longV[51..48])
    @Test
    fun get_55_52() = assertEquals(0x3, longV[55..52])
    @Test
    fun get_59_56() = assertEquals(0xA, longV[59..56])
    @Test
    fun get_63_60() = assertEquals(0x9, longV[63..60])

    @Test
    fun insertField_0xFB() = assertEquals(0xFB, insertField(0xF2, 0x0B, 3..0))
    @Test
    fun insertField_0xF0() = assertEquals(0xF0, insertField(0xF2, 0, 3..0))

    @Test
    fun int_insert_0000_00AA() = assertEquals(0x0000_00AA, insert(0xAA, 7..0))
    @Test
    fun int_insert_0000_0AA0() = assertEquals(0x0000_0AA0, insert(0xAA, 11..4))
    @Test
    fun int_insert_0000_AA00() = assertEquals(0x0000_AA00, insert(0xAA, 15..8))
    @Test
    fun int_insert_000A_A000() = assertEquals(0x000A_A000, insert(0xAA, 19..12))
    @Test
    fun int_insert_00AA_0000() = assertEquals(0x00AA_0000, insert(0xAA, 23..16))
    @Test
    fun int_insert_0AA0_0000() = assertEquals(0x0AA0_0000, insert(0xAA, 27..20))
    @Test
    fun int_insert_AA00_0000() = assertEquals(0xAA00_0000.int, insert(0xAA, 31..24))

    @Test
    fun long_insert_0000_0000_0000_00AA() = assertEquals(0x0000_0000_0000_00AA, insert(0xAAL, 7..0))
    @Test
    fun long_insert_0000_0000_0000_0AA0() = assertEquals(0x0000_0000_0000_0AA0, insert(0xAAL, 11..4))
    @Test
    fun long_insert_0000_0000_0000_AA00() = assertEquals(0x0000_0000_0000_AA00, insert(0xAAL, 15..8))
    @Test
    fun long_insert_0000_0000_000A_A000() = assertEquals(0x0000_0000_000A_A000, insert(0xAAL, 19..12))
    @Test
    fun long_insert_0000_0000_00AA_0000() = assertEquals(0x0000_0000_00AA_0000, insert(0xAAL, 23..16))
    @Test
    fun long_insert_0000_0000_0AA0_0000() = assertEquals(0x0000_0000_0AA0_0000, insert(0xAAL, 27..20))
    @Test
    fun long_insert_0000_0000_AA00_0000() = assertEquals(0x0000_0000_AA00_0000, insert(0xAAL, 31..24))

    @Test
    fun long_insert_0000_000A_A000_0000() = assertEquals(0x0000_000A_A000_0000, insert(0xAAL, 35..28))

    @Test
    fun long_insert_0000_00AA_0000_0000() = assertEquals(0x0000_00AA_0000_0000, insert(0xAAL, 39..32))
    @Test
    fun long_insert_0000_0AA0_0000_0000() = assertEquals(0x0000_0AA0_0000_0000, insert(0xAAL, 43..36))
    @Test
    fun long_insert_0000_AA00_0000_0000() = assertEquals(0x0000_AA00_0000_0000, insert(0xAAL, 47..40))
    @Test
    fun long_insert_000A_A000_0000_0000() = assertEquals(0x000A_A000_0000_0000, insert(0xAAL, 51..44))
    @Test
    fun long_insert_00AA_0000_0000_0000() = assertEquals(0x00AA_0000_0000_0000, insert(0xAAL, 55..48))
    @Test
    fun long_insert_0AA0_0000_0000_0000() = assertEquals(0x0AA0_0000_0000_0000, insert(0xAAL, 59..52))
    @Test
    fun long_insert_AA00_0000_0000_0000() = assertEquals(-6196953087261802496, insert(0xAAL, 63..56))

    @Test
    fun uint_clr_0() = assertEquals(0xFEu, 0xFFu clr 0)
    @Test
    fun uint_clr_1() = assertEquals(0xFDu, 0xFFu clr 1)
    @Test
    fun uint_clr_2() = assertEquals(0xFBu, 0xFFu clr 2)
    @Test
    fun uint_clr_3() = assertEquals(0xF7u, 0xFFu clr 3)
    @Test
    fun uint_clr_4() = assertEquals(0xEFu, 0xFFu clr 4)
    @Test
    fun uint_clr_5() = assertEquals(0xDFu, 0xFFu clr 5)
    @Test
    fun uint_clr_6() = assertEquals(0xBFu, 0xFFu clr 6)
    @Test
    fun uint_clr_7() = assertEquals(0x7Fu, 0xFFu clr 7)

    @Test
    fun uint_set_0() = assertEquals(0x01u, 0u set 0)
    @Test
    fun uint_set_1() = assertEquals(0x02u, 0u set 1)
    @Test
    fun uint_set_2() = assertEquals(0x04u, 0u set 2)
    @Test
    fun uint_set_3() = assertEquals(0x08u, 0u set 3)
    @Test
    fun uint_set_4() = assertEquals(0x10u, 0u set 4)
    @Test
    fun uint_set_5() = assertEquals(0x20u, 0u set 5)
    @Test
    fun uint_set_6() = assertEquals(0x40u, 0u set 6)
    @Test
    fun uint_set_7() = assertEquals(0x80u, 0u set 7)

    @Test
    fun uint_toggle_0() = assertEquals(0xFEu, 0xFFu toggle 0)
    @Test
    fun uint_toggle_1() = assertEquals(0xFDu, 0xFFu toggle 1)
    @Test
    fun uint_toggle_2() = assertEquals(0xFBu, 0xFFu toggle 2)
    @Test
    fun uint_toggle_3() = assertEquals(0xF7u, 0xFFu toggle 3)
    @Test
    fun uint_toggle_4() = assertEquals(0xEFu, 0xFFu toggle 4)
    @Test
    fun uint_toggle_5() = assertEquals(0xDFu, 0xFFu toggle 5)
    @Test
    fun uint_toggle_6() = assertEquals(0xBFu, 0xFFu toggle 6)
    @Test
    fun uint_toggle_7() = assertEquals(0x7Fu, 0xFFu toggle 7)

    @Test
    fun ulong_clr_0() = assertEquals(0xFEu, 0xFFuL clr 0)
    @Test
    fun ulong_clr_1() = assertEquals(0xFDu, 0xFFuL clr 1)
    @Test
    fun ulong_clr_2() = assertEquals(0xFBu, 0xFFuL clr 2)
    @Test
    fun ulong_clr_3() = assertEquals(0xF7u, 0xFFuL clr 3)
    @Test
    fun ulong_clr_4() = assertEquals(0xEFu, 0xFFuL clr 4)
    @Test
    fun ulong_clr_5() = assertEquals(0xDFu, 0xFFuL clr 5)
    @Test
    fun ulong_clr_6() = assertEquals(0xBFu, 0xFFuL clr 6)
    @Test
    fun ulong_clr_7() = assertEquals(0x7Fu, 0xFFuL clr 7)

    @Test
    fun ulong_set_0() = assertEquals(0x01u, 0uL set 0)
    @Test
    fun ulong_set_1() = assertEquals(0x02u, 0uL set 1)
    @Test
    fun ulong_set_2() = assertEquals(0x04u, 0uL set 2)
    @Test
    fun ulong_set_3() = assertEquals(0x08u, 0uL set 3)
    @Test
    fun ulong_set_4() = assertEquals(0x10u, 0uL set 4)
    @Test
    fun ulong_set_5() = assertEquals(0x20u, 0uL set 5)
    @Test
    fun ulong_set_6() = assertEquals(0x40u, 0uL set 6)
    @Test
    fun ulong_set_7() = assertEquals(0x80u, 0uL set 7)

    @Test
    fun ulong_toggle_0() = assertEquals(0xFEu, 0xFFuL toggle 0)
    @Test
    fun ulong_toggle_1() = assertEquals(0xFDu, 0xFFuL toggle 1)
    @Test
    fun ulong_toggle_2() = assertEquals(0xFBu, 0xFFuL toggle 2)
    @Test
    fun ulong_toggle_3() = assertEquals(0xF7u, 0xFFuL toggle 3)
    @Test
    fun ulong_toggle_4() = assertEquals(0xEFu, 0xFFuL toggle 4)
    @Test
    fun ulong_toggle_5() = assertEquals(0xDFu, 0xFFuL toggle 5)
    @Test
    fun ulong_toggle_6() = assertEquals(0xBFu, 0xFFuL toggle 6)
    @Test
    fun ulong_toggle_7() = assertEquals(0x7Fu, 0xFFuL toggle 7)

    @Test
    fun int_clr_0() = assertEquals(0xFE, 0xFF clr 0)
    @Test
    fun int_clr_1() = assertEquals(0xFD, 0xFF clr 1)
    @Test
    fun int_clr_2() = assertEquals(0xFB, 0xFF clr 2)
    @Test
    fun int_clr_3() = assertEquals(0xF7, 0xFF clr 3)
    @Test
    fun int_clr_4() = assertEquals(0xEF, 0xFF clr 4)
    @Test
    fun int_clr_5() = assertEquals(0xDF, 0xFF clr 5)
    @Test
    fun int_clr_6() = assertEquals(0xBF, 0xFF clr 6)
    @Test
    fun int_clr_7() = assertEquals(0x7F, 0xFF clr 7)

    @Test
    fun int_set_0() = assertEquals(0x01, 0 set 0)
    @Test
    fun int_set_1() = assertEquals(0x02, 0 set 1)
    @Test
    fun int_set_2() = assertEquals(0x04, 0 set 2)
    @Test
    fun int_set_3() = assertEquals(0x08, 0 set 3)
    @Test
    fun int_set_4() = assertEquals(0x10, 0 set 4)
    @Test
    fun int_set_5() = assertEquals(0x20, 0 set 5)
    @Test
    fun int_set_6() = assertEquals(0x40, 0 set 6)
    @Test
    fun int_set_7() = assertEquals(0x80, 0 set 7)

    @Test
    fun int_toggle_0() = assertEquals(0xFE, 0xFF toggle 0)
    @Test
    fun int_toggle_1() = assertEquals(0xFD, 0xFF toggle 1)
    @Test
    fun int_toggle_2() = assertEquals(0xFB, 0xFF toggle 2)
    @Test
    fun int_toggle_3() = assertEquals(0xF7, 0xFF toggle 3)
    @Test
    fun int_toggle_4() = assertEquals(0xEF, 0xFF toggle 4)
    @Test
    fun int_toggle_5() = assertEquals(0xDF, 0xFF toggle 5)
    @Test
    fun int_toggle_6() = assertEquals(0xBF, 0xFF toggle 6)
    @Test
    fun int_toggle_7() = assertEquals(0x7F, 0xFF toggle 7)

    @Test
    fun long_clr_0() = assertEquals(0xFE, 0xFFL clr 0)
    @Test
    fun long_clr_1() = assertEquals(0xFD, 0xFFL clr 1)
    @Test
    fun long_clr_2() = assertEquals(0xFB, 0xFFL clr 2)
    @Test
    fun long_clr_3() = assertEquals(0xF7, 0xFFL clr 3)
    @Test
    fun long_clr_4() = assertEquals(0xEF, 0xFFL clr 4)
    @Test
    fun long_clr_5() = assertEquals(0xDF, 0xFFL clr 5)
    @Test
    fun long_clr_6() = assertEquals(0xBF, 0xFFL clr 6)
    @Test
    fun long_clr_7() = assertEquals(0x7F, 0xFFL clr 7)

    @Test
    fun long_set_0() = assertEquals(0x01, 0L set 0)
    @Test
    fun long_set_1() = assertEquals(0x02, 0L set 1)
    @Test
    fun long_set_2() = assertEquals(0x04, 0L set 2)
    @Test
    fun long_set_3() = assertEquals(0x08, 0L set 3)
    @Test
    fun long_set_4() = assertEquals(0x10, 0L set 4)
    @Test
    fun long_set_5() = assertEquals(0x20, 0L set 5)
    @Test
    fun long_set_6() = assertEquals(0x40, 0L set 6)
    @Test
    fun long_set_7() = assertEquals(0x80, 0L set 7)

    @Test
    fun long_toggle_0() = assertEquals(0xFE, 0xFFL toggle 0)
    @Test
    fun long_toggle_1() = assertEquals(0xFD, 0xFFL toggle 1)
    @Test
    fun long_toggle_2() = assertEquals(0xFB, 0xFFL toggle 2)
    @Test
    fun long_toggle_3() = assertEquals(0xF7, 0xFFL toggle 3)
    @Test
    fun long_toggle_4() = assertEquals(0xEF, 0xFFL toggle 4)
    @Test
    fun long_toggle_5() = assertEquals(0xDF, 0xFFL toggle 5)
    @Test
    fun long_toggle_6() = assertEquals(0xBF, 0xFFL toggle 6)
    @Test
    fun long_toggle_7() = assertEquals(0x7F, 0xFFL toggle 7)

    @Test
    fun cat_0xFAL() = assertEquals(0xFAL, cat(0xFL, 0xAL, 3))
    @Test
    fun cat_0xFA() = assertEquals(0xFA, cat(0xF, 0xA, 3))

    @Test
    fun ulong_swap64() = assertEquals(0xDDCCBBAA66778899uL, 0x99887766AABBCCDDuL.swap64())
    @Test
    fun ulong_swap32() = assertEquals(0xDDCCBBAAuL, 0xAABBCCDDuL.swap32())
    @Test
    fun ulong_swap16() = assertEquals(0xBBAAuL, 0xAABBuL.swap16())

    @Test
    fun uint_swap32() = assertEquals(0xDDCCBBAAu, 0xAABBCCDDu.swap32())
    @Test
    fun uint_swap16() = assertEquals(0xBBAAu, 0xAABBu.swap16())

    @Test
    fun long_swap64() = assertEquals(-2464388555540559719, (-7383520306239124259).swap64())
    @Test
    fun long_swap32() = assertEquals(3721182122, 2864434397L.swap32())
    @Test
    fun long_swap16() = assertEquals(48042, 43707L.swap16())

    @Test
    fun int_swap32() = assertEquals(-573785174, (-1430532899).swap32())
    @Test
    fun int_swap16() = assertEquals(48042, 43707.swap16())


    @Test
    fun ulong_rotr64_4() = assertEquals(0xD99887766AABBCCDuL, 0x99887766AABBCCDDuL.rotr64(4))
    @Test
    fun ulong_rotr64_8() = assertEquals(0xDD99887766AABBCCuL, 0x99887766AABBCCDDuL.rotr64(8))
    @Test
    fun ulong_rotr64_12() = assertEquals(0xCDD99887766AABBCuL, 0x99887766AABBCCDDuL.rotr64(12))
    @Test
    fun ulong_rotr64_16() = assertEquals(0xCCDD99887766AABBuL, 0x99887766AABBCCDDuL.rotr64(16))
    @Test
    fun ulong_rotr64_20() = assertEquals(0xBCCDD99887766AABuL, 0x99887766AABBCCDDuL.rotr64(20))
    @Test
    fun ulong_rotr64_24() = assertEquals(0xBBCCDD99887766AAuL, 0x99887766AABBCCDDuL.rotr64(24))
    @Test
    fun ulong_rotr64_28() = assertEquals(0xABBCCDD99887766AuL, 0x99887766AABBCCDDuL.rotr64(28))
    @Test
    fun ulong_rotr64_32() = assertEquals(0xAABBCCDD99887766uL, 0x99887766AABBCCDDuL.rotr64(32))
    @Test
    fun ulong_rotr64_36() = assertEquals(0x6AABBCCDD9988776uL, 0x99887766AABBCCDDuL.rotr64(36))
    @Test
    fun ulong_rotr64_40() = assertEquals(0x66AABBCCDD998877uL, 0x99887766AABBCCDDuL.rotr64(40))
    @Test
    fun ulong_rotr64_44() = assertEquals(0x766AABBCCDD99887uL, 0x99887766AABBCCDDuL.rotr64(44))
    @Test
    fun ulong_rotr64_48() = assertEquals(0x7766AABBCCDD9988uL, 0x99887766AABBCCDDuL.rotr64(48))
    @Test
    fun ulong_rotr64_52() = assertEquals(0x87766AABBCCDD998uL, 0x99887766AABBCCDDuL.rotr64(52))
    @Test
    fun ulong_rotr64_56() = assertEquals(0x887766AABBCCDD99uL, 0x99887766AABBCCDDuL.rotr64(56))
    @Test
    fun ulong_rotr64_60() = assertEquals(0x9887766AABBCCDD9uL, 0x99887766AABBCCDDuL.rotr64(60))
    @Test
    fun ulong_rotr64_64() = assertEquals(0x99887766AABBCCDDuL, 0x99887766AABBCCDDuL.rotr64(64))


    @Test
    fun ulong_rotr32_4() = assertEquals(0xDAABBCCDuL, 0xAABBCCDDuL.rotr32(4))
    @Test
    fun ulong_rotr32_8() = assertEquals(0xDDAABBCCuL, 0xAABBCCDDuL.rotr32(8))
    @Test
    fun ulong_rotr32_12() = assertEquals(0xCDDAABBCuL, 0xAABBCCDDuL.rotr32(12))
    @Test
    fun ulong_rotr32_16() = assertEquals(0xCCDDAABBuL, 0xAABBCCDDuL.rotr32(16))
    @Test
    fun ulong_rotr32_20() = assertEquals(0xBCCDDAABuL, 0xAABBCCDDuL.rotr32(20))
    @Test
    fun ulong_rotr32_24() = assertEquals(0xBBCCDDAAuL, 0xAABBCCDDuL.rotr32(24))
    @Test
    fun ulong_rotr32_28() = assertEquals(0xABBCCDDAuL, 0xAABBCCDDuL.rotr32(28))
    @Test
    fun ulong_rotr32_32() = assertEquals(0xAABBCCDDuL, 0xAABBCCDDuL.rotr32(32))


    @Test
    fun uint_rotr32_4() = assertEquals(0xDAABBCCDu, 0xAABBCCDDu.rotr32(4))
    @Test
    fun uint_rotr32_8() = assertEquals(0xDDAABBCCu, 0xAABBCCDDu.rotr32(8))
    @Test
    fun uint_rotr32_12() = assertEquals(0xCDDAABBCu, 0xAABBCCDDu.rotr32(12))
    @Test
    fun uint_rotr32_16() = assertEquals(0xCCDDAABBu, 0xAABBCCDDu.rotr32(16))
    @Test
    fun uint_rotr32_20() = assertEquals(0xBCCDDAABu, 0xAABBCCDDu.rotr32(20))
    @Test
    fun uint_rotr32_24() = assertEquals(0xBBCCDDAAu, 0xAABBCCDDu.rotr32(24))
    @Test
    fun uint_rotr32_28() = assertEquals(0xABBCCDDAu, 0xAABBCCDDu.rotr32(28))
    @Test
    fun uint_rotr32_32() = assertEquals(0xAABBCCDDu, 0xAABBCCDDu.rotr32(32))


    @Test
    fun ulong_rotl64_4() = assertEquals(0x9887766AABBCCDD9uL, 0x99887766AABBCCDDuL.rotl64(4))
    @Test
    fun ulong_rotl64_8() = assertEquals(0x887766AABBCCDD99uL, 0x99887766AABBCCDDuL.rotl64(8))
    @Test
    fun ulong_rotl64_12() = assertEquals(0x87766AABBCCDD998uL, 0x99887766AABBCCDDuL.rotl64(12))
    @Test
    fun ulong_rotl64_16() = assertEquals(0x7766AABBCCDD9988uL, 0x99887766AABBCCDDuL.rotl64(16))
    @Test
    fun ulong_rotl64_20() = assertEquals(0x766AABBCCDD99887uL, 0x99887766AABBCCDDuL.rotl64(20))
    @Test
    fun ulong_rotl64_24() = assertEquals(0x66AABBCCDD998877uL, 0x99887766AABBCCDDuL.rotl64(24))
    @Test
    fun ulong_rotl64_28() = assertEquals(0x6AABBCCDD9988776uL, 0x99887766AABBCCDDuL.rotl64(28))
    @Test
    fun ulong_rotl64_32() = assertEquals(0xAABBCCDD99887766uL, 0x99887766AABBCCDDuL.rotl64(32))
    @Test
    fun ulong_rotl64_36() = assertEquals(0xABBCCDD99887766AuL, 0x99887766AABBCCDDuL.rotl64(36))
    @Test
    fun ulong_rotl64_40() = assertEquals(0xBBCCDD99887766AAuL, 0x99887766AABBCCDDuL.rotl64(40))
    @Test
    fun ulong_rotl64_44() = assertEquals(0xBCCDD99887766AABuL, 0x99887766AABBCCDDuL.rotl64(44))
    @Test
    fun ulong_rotl64_48() = assertEquals(0xCCDD99887766AABBuL, 0x99887766AABBCCDDuL.rotl64(48))
    @Test
    fun ulong_rotl64_52() = assertEquals(0xCDD99887766AABBCuL, 0x99887766AABBCCDDuL.rotl64(52))
    @Test
    fun ulong_rotl64_56() = assertEquals(0xDD99887766AABBCCuL, 0x99887766AABBCCDDuL.rotl64(56))
    @Test
    fun ulong_rotl64_60() = assertEquals(0xD99887766AABBCCDuL, 0x99887766AABBCCDDuL.rotl64(60))
    @Test
    fun ulong_rotl64_64() = assertEquals(0x99887766AABBCCDDuL, 0x99887766AABBCCDDuL.rotl64(64))


    @Test
    fun uint_rotl32_4() = assertEquals(0xABBCCDDAu, 0xAABBCCDDu.rotl32(4))
    @Test
    fun uint_rotl32_8() = assertEquals(0xBBCCDDAAu, 0xAABBCCDDu.rotl32(8))
    @Test
    fun uint_rotl32_12() = assertEquals(0xBCCDDAABu, 0xAABBCCDDu.rotl32(12))
    @Test
    fun uint_rotl32_16() = assertEquals(0xCCDDAABBu, 0xAABBCCDDu.rotl32(16))
    @Test
    fun uint_rotl32_20() = assertEquals(0xCDDAABBCu, 0xAABBCCDDu.rotl32(20))
    @Test
    fun uint_rotl32_24() = assertEquals(0xDDAABBCCu, 0xAABBCCDDu.rotl32(24))
    @Test
    fun uint_rotl32_28() = assertEquals(0xDAABBCCDu, 0xAABBCCDDu.rotl32(28))
    @Test
    fun uint_rotl32_32() = assertEquals(0xAABBCCDDu, 0xAABBCCDDu.rotl32(32))

    @Test
    fun ulong_signext_00() = assertEquals(0xFFFF_FFFF_FFFF_FFFFuL, 0x0001uL signextRenameMeAfter 0)
    @Test
    fun ulong_signext_01() = assertEquals(0xFFFF_FFFF_FFFF_FFFEuL, 0x0002uL signextRenameMeAfter 1)
    @Test
    fun ulong_signext_02() = assertEquals(0xFFFF_FFFF_FFFF_FFFCuL, 0x0004uL signextRenameMeAfter 2)
    @Test
    fun ulong_signext_03() = assertEquals(0xFFFF_FFFF_FFFF_FFF8uL, 0x0008uL signextRenameMeAfter 3)
    @Test
    fun ulong_signext_04() = assertEquals(0xFFFF_FFFF_FFFF_FFF0uL, 0x0010uL signextRenameMeAfter 4)
    @Test
    fun ulong_signext_05() = assertEquals(0xFFFF_FFFF_FFFF_FFE0uL, 0x0020uL signextRenameMeAfter 5)
    @Test
    fun ulong_signext_06() = assertEquals(0xFFFF_FFFF_FFFF_FFC0uL, 0x0040uL signextRenameMeAfter 6)
    @Test
    fun ulong_signext_07() = assertEquals(0xFFFF_FFFF_FFFF_FF80uL, 0x0080uL signextRenameMeAfter 7)
    @Test
    fun ulong_signext_08() = assertEquals(0xFFFF_FFFF_FFFF_FF00uL, 0x0100uL signextRenameMeAfter 8)
    @Test
    fun ulong_signext_09() = assertEquals(0xFFFF_FFFF_FFFF_FE00uL, 0x0200uL signextRenameMeAfter 9)
    @Test
    fun ulong_signext_10() = assertEquals(0xFFFF_FFFF_FFFF_FC00uL, 0x0400uL signextRenameMeAfter 10)
    @Test
    fun ulong_signext_11() = assertEquals(0xFFFF_FFFF_FFFF_F800uL, 0x0800uL signextRenameMeAfter 11)
    @Test
    fun ulong_signext_12() = assertEquals(0xFFFF_FFFF_FFFF_F000uL, 0x1000uL signextRenameMeAfter 12)
    @Test
    fun ulong_signext_13() = assertEquals(0xFFFF_FFFF_FFFF_E000uL, 0x2000uL signextRenameMeAfter 13)
    @Test
    fun ulong_signext_14() = assertEquals(0xFFFF_FFFF_FFFF_C000uL, 0x4000uL signextRenameMeAfter 14)
    @Test
    fun ulong_signext_15() = assertEquals(0xFFFF_FFFF_FFFF_8000uL, 0x8000uL signextRenameMeAfter 15)

    @Test
    fun ulong_signext_16() = assertEquals(0xFFFF_FFFF_FFFF_0000uL, 0x0001_0000uL signextRenameMeAfter 16)
    @Test
    fun ulong_signext_17() = assertEquals(0xFFFF_FFFF_FFFE_0000uL, 0x0002_0000uL signextRenameMeAfter 17)
    @Test
    fun ulong_signext_18() = assertEquals(0xFFFF_FFFF_FFFC_0000uL, 0x0004_0000uL signextRenameMeAfter 18)
    @Test
    fun ulong_signext_19() = assertEquals(0xFFFF_FFFF_FFF8_0000uL, 0x0008_0000uL signextRenameMeAfter 19)
    @Test
    fun ulong_signext_20() = assertEquals(0xFFFF_FFFF_FFF0_0000uL, 0x0010_0000uL signextRenameMeAfter 20)
    @Test
    fun ulong_signext_21() = assertEquals(0xFFFF_FFFF_FFE0_0000uL, 0x0020_0000uL signextRenameMeAfter 21)
    @Test
    fun ulong_signext_22() = assertEquals(0xFFFF_FFFF_FFC0_0000uL, 0x0040_0000uL signextRenameMeAfter 22)
    @Test
    fun ulong_signext_23() = assertEquals(0xFFFF_FFFF_FF80_0000uL, 0x0080_0000uL signextRenameMeAfter 23)
    @Test
    fun ulong_signext_24() = assertEquals(0xFFFF_FFFF_FF00_0000uL, 0x0100_0000uL signextRenameMeAfter 24)
    @Test
    fun ulong_signext_25() = assertEquals(0xFFFF_FFFF_FE00_0000uL, 0x0200_0000uL signextRenameMeAfter 25)
    @Test
    fun ulong_signext_26() = assertEquals(0xFFFF_FFFF_FC00_0000uL, 0x0400_0000uL signextRenameMeAfter 26)
    @Test
    fun ulong_signext_27() = assertEquals(0xFFFF_FFFF_F800_0000uL, 0x0800_0000uL signextRenameMeAfter 27)
    @Test
    fun ulong_signext_28() = assertEquals(0xFFFF_FFFF_F000_0000uL, 0x1000_0000uL signextRenameMeAfter 28)
    @Test
    fun ulong_signext_29() = assertEquals(0xFFFF_FFFF_E000_0000uL, 0x2000_0000uL signextRenameMeAfter 29)
    @Test
    fun ulong_signext_30() = assertEquals(0xFFFF_FFFF_C000_0000uL, 0x4000_0000uL signextRenameMeAfter 30)
    @Test
    fun ulong_signext_31() = assertEquals(0xFFFF_FFFF_8000_0000uL, 0x8000_0000uL signextRenameMeAfter 31)

    @Test
    fun ulong_signext_32() = assertEquals(0xFFFF_FFFF_0000_0000uL, 0x0000_0001_0000_0000uL signextRenameMeAfter 32)
    @Test
    fun ulong_signext_33() = assertEquals(0xFFFF_FFFE_0000_0000uL, 0x0000_0002_0000_0000uL signextRenameMeAfter 33)
    @Test
    fun ulong_signext_34() = assertEquals(0xFFFF_FFFC_0000_0000uL, 0x0000_0004_0000_0000uL signextRenameMeAfter 34)
    @Test
    fun ulong_signext_35() = assertEquals(0xFFFF_FFF8_0000_0000uL, 0x0000_0008_0000_0000uL signextRenameMeAfter 35)
    @Test
    fun ulong_signext_36() = assertEquals(0xFFFF_FFF0_0000_0000uL, 0x0000_0010_0000_0000uL signextRenameMeAfter 36)
    @Test
    fun ulong_signext_37() = assertEquals(0xFFFF_FFE0_0000_0000uL, 0x0000_0020_0000_0000uL signextRenameMeAfter 37)
    @Test
    fun ulong_signext_38() = assertEquals(0xFFFF_FFC0_0000_0000uL, 0x0000_0040_0000_0000uL signextRenameMeAfter 38)
    @Test
    fun ulong_signext_39() = assertEquals(0xFFFF_FF80_0000_0000uL, 0x0000_0080_0000_0000uL signextRenameMeAfter 39)
    @Test
    fun ulong_signext_40() = assertEquals(0xFFFF_FF00_0000_0000uL, 0x0000_0100_0000_0000uL signextRenameMeAfter 40)
    @Test
    fun ulong_signext_41() = assertEquals(0xFFFF_FE00_0000_0000uL, 0x0000_0200_0000_0000uL signextRenameMeAfter 41)
    @Test
    fun ulong_signext_42() = assertEquals(0xFFFF_FC00_0000_0000uL, 0x0000_0400_0000_0000uL signextRenameMeAfter 42)
    @Test
    fun ulong_signext_43() = assertEquals(0xFFFF_F800_0000_0000uL, 0x0000_0800_0000_0000uL signextRenameMeAfter 43)
    @Test
    fun ulong_signext_44() = assertEquals(0xFFFF_F000_0000_0000uL, 0x0000_1000_0000_0000uL signextRenameMeAfter 44)
    @Test
    fun ulong_signext_45() = assertEquals(0xFFFF_E000_0000_0000uL, 0x0000_2000_0000_0000uL signextRenameMeAfter 45)
    @Test
    fun ulong_signext_46() = assertEquals(0xFFFF_C000_0000_0000uL, 0x0000_4000_0000_0000uL signextRenameMeAfter 46)
    @Test
    fun ulong_signext_47() = assertEquals(0xFFFF_8000_0000_0000uL, 0x0000_8000_0000_0000uL signextRenameMeAfter 47)

    @Test
    fun ulong_signext_48() = assertEquals(0xFFFF_0000_0000_0000uL, 0x0001_0000_0000_0000uL signextRenameMeAfter 48)
    @Test
    fun ulong_signext_49() = assertEquals(0xFFFE_0000_0000_0000uL, 0x0002_0000_0000_0000uL signextRenameMeAfter 49)
    @Test
    fun ulong_signext_50() = assertEquals(0xFFFC_0000_0000_0000uL, 0x0004_0000_0000_0000uL signextRenameMeAfter 50)
    @Test
    fun ulong_signext_51() = assertEquals(0xFFF8_0000_0000_0000uL, 0x0008_0000_0000_0000uL signextRenameMeAfter 51)
    @Test
    fun ulong_signext_52() = assertEquals(0xFFF0_0000_0000_0000uL, 0x0010_0000_0000_0000uL signextRenameMeAfter 52)
    @Test
    fun ulong_signext_53() = assertEquals(0xFFE0_0000_0000_0000uL, 0x0020_0000_0000_0000uL signextRenameMeAfter 53)
    @Test
    fun ulong_signext_54() = assertEquals(0xFFC0_0000_0000_0000uL, 0x0040_0000_0000_0000uL signextRenameMeAfter 54)
    @Test
    fun ulong_signext_55() = assertEquals(0xFF80_0000_0000_0000uL, 0x0080_0000_0000_0000uL signextRenameMeAfter 55)
    @Test
    fun ulong_signext_56() = assertEquals(0xFF00_0000_0000_0000uL, 0x0100_0000_0000_0000uL signextRenameMeAfter 56)
    @Test
    fun ulong_signext_57() = assertEquals(0xFE00_0000_0000_0000uL, 0x0200_0000_0000_0000uL signextRenameMeAfter 57)
    @Test
    fun ulong_signext_58() = assertEquals(0xFC00_0000_0000_0000uL, 0x0400_0000_0000_0000uL signextRenameMeAfter 58)
    @Test
    fun ulong_signext_59() = assertEquals(0xF800_0000_0000_0000uL, 0x0800_0000_0000_0000uL signextRenameMeAfter 59)
    @Test
    fun ulong_signext_60() = assertEquals(0xF000_0000_0000_0000uL, 0x1000_0000_0000_0000uL signextRenameMeAfter 60)
    @Test
    fun ulong_signext_61() = assertEquals(0xE000_0000_0000_0000uL, 0x2000_0000_0000_0000uL signextRenameMeAfter 61)
    @Test
    fun ulong_signext_62() = assertEquals(0xC000_0000_0000_0000uL, 0x4000_0000_0000_0000uL signextRenameMeAfter 62)
    @Test
    fun ulong_signext_63() = assertEquals(0x8000_0000_0000_0000uL, 0x8000_0000_0000_0000uL signextRenameMeAfter 63)

    @Test
    fun isIntegerOverflow() {
        // TODO
    }

    @Test
    fun overflowOfXbits() {
        assertEquals(123123123uL, 123123123uL[63..0])
        assertEquals(0xFFFFFFFF_FFFFFFFFuL, 0xFFFFFFFF_FFFFFFFFuL[63..0])
        assertEquals(0xFFFFFFFF_FFFFFuL, 0xFFFFFFFF_FFFFFFFFuL[69..12])
        assertEquals(0x12_34uL, 0xABCDEF12_34567890uL[39 .. 24])

        assertEquals(0x0uL, 0xFFFFFFFF_FFFFFFFFuL[999..99])

        assertEquals(15, 15[31..0])
    }

    @Test
    fun indexesXBits() {
        assertEquals(1uL, 0xFFFF_FFFF_FFFF_FFFFuL[63..63])
        assertEquals(1uL, 0xFFFF_FFFF_FFFF_FFFFuL[0..0])
        assertEquals(1uL, 0xFFFF_FFFF_FFFF_FFFFuL[31..31])
        assertEquals(1uL, 0xFFFF_FFFF_FFFF_FFFFuL[32..32])

        assertEquals(0u, 0xFFFF_FFFFu[63..63])
        assertEquals(1u, 0xFFFF_FFFFu[0..0])
        assertEquals(1u, 0xFFFF_FFFFu[31..31])
        assertEquals(0u, 0xFFFF_FFFFu[32..32])
    }

    @Test
    fun performanceXbits() {
        Benchmark().bench {
            123123123uL[63..0]
            0xFFFFFFFF_FFFFFFFFuL[63..0]
            0xFFFFFFFF_FFFFFFFFuL[69..12]
            0xABCDEF12_34567890uL[39 .. 24]
            0xFFFFFFFF_FFFFFFFFuL[999..99]
            15[31..0]
        }.also {
            println("mean: ${it.inWholeMicroseconds}")
        }
    }
}