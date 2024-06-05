package ru.inforion.lab403.common.spark

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.junit.jupiter.api.Test
import org.objenesis.strategy.StdInstantiatorStrategy
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals


internal class NestedTest {
    open class ModulePorts(val module: String) {
        private val map = HashMap<String, ModulePorts.APort>()

        inner class APort(val name: String, val size: ULong, val type: String = "UNK") {
            override fun toString() = "APort(name=$name, size=$size)"

            override fun hashCode(): Int {
                var result = name.hashCode()
                result = 31 * result + size.hashCode()
                result = 31 * result + type.hashCode()
                return result
            }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is APort) return false

                if (name != other.name) return false
                if (size != other.size) return false
                if (type != other.type) return false

                return true
            }
        }
    }

    data class Module(val ord: Int) {
        inner class Ports : ModulePorts("this") {
            val mem = APort("mem", 0x10u)
            val irq = APort("irq", 0x10u)

            override fun toString() = "Ports(mem=$mem, irq=$irq)"
        }

        val ports = Ports()

        override fun toString() = "Module(ord=$ord ports=$ports)"
    }

    @Test
    fun testNestedSerialize() {
        com.esotericsoftware.minlog.Log.TRACE()

        val kryo = Kryo()
        kryo.fieldSerializerConfig.isIgnoreSyntheticFields = false
        kryo.instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())

        val object1 = Module(1)

        val outStream = ByteArrayOutputStream()
        val output = Output(outStream, 1024)
        kryo.writeClassAndObject(output, object1)
        output.flush()
        val out = outStream.toByteArray()
        val input = Input(out)
        val object2 = kryo.readClassAndObject(input) as Module

        assertEquals(object1.ord, object2.ord)
        assertEquals(object1.ports.irq, object2.ports.irq)
        assertEquals(object1.ports.mem, object2.ports.mem)
    }
}