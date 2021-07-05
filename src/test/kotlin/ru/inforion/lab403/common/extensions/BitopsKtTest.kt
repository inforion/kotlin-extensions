package ru.inforion.lab403.common.extensions

import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFails

/**
 * Created by davydov_vn on 22/04/19.
 */

class BitopsKtTest {
    private val x = 0xDEADBEEF

    @Test fun getTest_p0_p4() = assertEquals(0xF, x[3..0])
    @Test fun getTest_p4_p8() = assertEquals(0xE, x[7..4])
    @Test fun getTest_p8_p12() = assertEquals(0xE, x[11..8])
    @Test fun getTest_p12_p16() = assertEquals(0xB, x[15..12])
    @Test fun getTest_p16_p20() = assertEquals(0xD, x[19..16])
    @Test fun getTest_p20_p24() = assertEquals(0xA, x[23..20])
    @Test fun getTest_p24_p28() = assertEquals(0xE, x[27..24])
    @Test fun getTest_p28_p32() = assertEquals(0xD, x[31..28])

    @Test fun bitMask() {
//        assertEquals(bitMask(0), 0x01)
        assertFails { bitMask64(0) }
        assertEquals(bitMask64(1), 0x01)
        assertEquals(bitMask64(2), 0x03)
        assertEquals(bitMask64(3), 0x07)
        assertEquals(bitMask64(4), 0x0F)
        assertEquals(bitMask64(5), 0x1F)
        assertEquals(bitMask64(6), 0x3F)
        assertEquals(bitMask64(7), 0x7F)

        assertEquals(bitMask64(11), 0x0000_07FF)
        assertEquals(bitMask64(15), 0x0000_7FFF)
        assertEquals(bitMask64(19), 0x0007_FFFF)
        assertEquals(bitMask64(23), 0x007F_FFFF)
        assertEquals(bitMask64(27), 0x07FF_FFFF)
        assertEquals(bitMask64(31), 0x7FFF_FFFF)
        assertEquals(bitMask64(32), 0xFFFF_FFFF)
        assertEquals(bitMask64(64), -1)
        assertFails { bitMask64(65) }

//        assertEquals(bitMask(63), "FFFFFFFFFFFFFFFF".hexAsULong)
    }

    @Test fun bitMaskRange() {
        assertEquals(bitMask64(7..4), 0xF0)
        assertEquals(bitMask64(7..0), 0xFF)

        assertEquals(bitMask64(11..0), 0x0000_0FFF)
        assertEquals(bitMask64(15..0), 0x0000_FFFF)
        assertEquals(bitMask64(19..0), 0x000F_FFFF)
        assertEquals(bitMask64(23..0), 0x00FF_FFFF)
        assertEquals(bitMask64(27..0), 0x0FFF_FFFF)
        assertEquals(bitMask64(31..0), 0xFFFF_FFFF)

        assertEquals(bitMask64(31..16), 0xFFFF_0000)

        assertEquals(bitMask64(63..0), "FFFFFFFFFFFFFFFF".ulongByHex)
    }

    @Test fun maskRange() {
        assertEquals(0xAD mask 7..4, 0xA0)
        assertEquals(0xAD mask 7..0, 0xAD)
        assertEquals(0xAD mask 6..5, 0x20)
        assertEquals(0xAD mask 4..3, 0x08)
        assertEquals(0xAD mask 5..2, 0x2C)
        assertEquals(0xAD mask 15..0, 0xAD)
        assertEquals(0xAD mask 63..14, 0x00)
        assertEquals(0xAD mask 3..2, 0x0C)
        assertEquals(0xAD mask 0..0, 0x01)
    }

    @Test fun signext() {
        // TODO
    }

    @Test fun bext() {
        // TODO
    }

    @Test fun insertBit() {
        // TODO
    }

    @Test fun clearBit() {
        assertEquals(0xFE, 0xFF clr 0)
        assertEquals(0xFD, 0xFF clr 1)
        assertEquals(0xFB, 0xFF clr 2)
        assertEquals(0xF7, 0xFF clr 3)
        assertEquals(0xEF, 0xFF clr 4)
        assertEquals(0xDF, 0xFF clr 5)
        assertEquals(0xBF, 0xFF clr 6)
        assertEquals(0x7F, 0xFF clr 7)
    }

    @Test fun setBit() {
        assertEquals(0x01, 0 set 0)
        assertEquals(0x02, 0 set 1)
        assertEquals(0x04, 0 set 2)
        assertEquals(0x08, 0 set 3)
        assertEquals(0x10, 0 set 4)
        assertEquals(0x20, 0 set 5)
        assertEquals(0x40, 0 set 6)
        assertEquals(0x80, 0 set 7)
    }

    @Test fun insertField() {
        assertEquals(insertField(0xF2, 0x0B, 3..0), 0xFB)
        assertEquals(insertField(0xF2, 0, 3..0), 0xF0)
    }

    @Test fun isIntegerOverflow() {
        // TODO
    }

    @Test fun swap32() {
        assertEquals(0xAABBCCDD.swap32(), 0xDDCCBBAA)
    }

    @Test fun swap16() {
        assertEquals(0xAABB.swap16(), 0xBBAA)
    }

    @Test fun cat() {
        assertEquals(0xFAL, cat(0xFL, 0xAL, 3))
        assertEquals(0xFA, cat(0xF, 0xA, 3))
    }
}