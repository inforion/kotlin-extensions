package ru.inforion.lab403.common.extensions

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by davydov_vn on 22/04/19.
 */

class DimsKtTest {
    @Test
    fun getHz() {
        assertEquals(1, 1.Hz)
    }

    @Test
    fun getKHz() {
        assertEquals(1_000, 1.kHz)
    }

    @Test
    fun getMHz() {
        assertEquals(1_000_000, 1.MHz)
    }

    @Test
    fun getGHz() {
        assertEquals(1_000_000_000, 1.GHz)
    }

    @Test
    fun to_Hz() {
        assertEquals(1, 1.to_Hz(from = Frequency.Hz))
        assertEquals(1_000, 1.to_Hz(from = Frequency.kHz))
        assertEquals(1_000_000, 1.to_Hz(from = Frequency.MHz))
        assertEquals(1_000_000_000, 1.to_Hz(from = Frequency.GHz))
    }

    @Test
    fun to_kHz() {
        assertEquals(1, 1_000.to_kHz(from = Frequency.Hz))
        assertEquals(1, 1.to_kHz(from = Frequency.kHz))
        assertEquals(1_000, 1.to_kHz(from = Frequency.MHz))
        assertEquals(1_000_000, 1.to_kHz(from = Frequency.GHz))
    }

    @Test
    fun to_MHz() {
        assertEquals(1, 1_000_000.to_MHz(from = Frequency.Hz))
        assertEquals(1, 1_000.to_MHz(from = Frequency.kHz))
        assertEquals(1, 1.to_MHz(from = Frequency.MHz))
        assertEquals(1_000, 1.to_MHz(from = Frequency.GHz))
    }

    @Test
    fun to_GHz() {
        assertEquals(1, 1_000_000_000.to_GHz(from = Frequency.Hz))
        assertEquals(1, 1_000_000.to_GHz(from = Frequency.kHz))
        assertEquals(1, 1_000.to_GHz(from = Frequency.MHz))
        assertEquals(1, 1.to_GHz(from = Frequency.GHz))
    }

    @Test
    fun getS() {
        assertEquals(1, 1.s)
    }

    @Test
    fun getMs() {
        assertEquals(1, 1_000.ms)
    }

    @Test
    fun getUs() {
        assertEquals(1, 1_000_000.us)
    }

    @Test
    fun getNs() {
        assertEquals(1, 1_000_000_000.ns)
    }

    @Test
    fun to_s() {
        assertEquals(1, 1.to_s(from = Time.s))
        assertEquals(1, 1_000.to_s(from = Time.ms))
        assertEquals(1, 1_000_000.to_s(from = Time.us))
        assertEquals(1, 1_000_000_000.to_s(from = Time.ns))
    }

    @Test
    fun to_ms() {
        assertEquals(1_000, 1.to_ms(from = Time.s))
        assertEquals(1, 1.to_ms(from = Time.ms))
        assertEquals(1, 1_000.to_ms(from = Time.us))
        assertEquals(1, 1_000_000.to_ms(from = Time.ns))
    }

    @Test
    fun to_us() {
        assertEquals(1_000_000, 1.to_us(from = Time.s))
        assertEquals(1_000, 1.to_us(from = Time.ms))
        assertEquals(1, 1.to_us(from = Time.us))
        assertEquals(1, 1_000.to_us(from = Time.ns))
    }

    @Test
    fun to_ns() {
        assertEquals(1_000_000_000, 1.to_ns(from = Time.s))
        assertEquals(1_000_000, 1.to_ns(from = Time.ms))
        assertEquals(1_000, 1.to_ns(from = Time.us))
        assertEquals(1, 1.to_ns(from = Time.ns))
    }
}