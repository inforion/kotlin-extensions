package ru.inforion.lab403.common.scripts

import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.junit.Test
import javax.script.ScriptEngineManager


class TestKotlinScript {

    val scriptManager = ScriptEngineManager()
    val factory = scriptManager.getEngineByName("kotlin").factory
    val engine = factory.scriptEngine as KotlinJsr223JvmLocalScriptEngine

    @Test
    fun testInterface() {

        val testCLass = engine.eval("""
            import ru.inforion.lab403.common.scripts.TestInterface
            
            class TestClass(val value: Any? = null) : TestInterface {
                override fun a(x: Int): Int {
                    println(x)
                    println(value)
                    return x
                }

                override fun b(s: String): String {
                    println(s)
                    println(value)
                    return s
                }
            }
            
            TestClass("ajfsnvapofnvoasjfnvaosnvanosvasv")
        """.trimIndent()) as TestInterface

        println(testCLass.a())
        println(testCLass.b())
        println(testCLass.a(1234))
        println(testCLass.b("different string"))
    }

}