package com.yaros.kitchen

import android.util.Base64
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun base64Test() {
        val ps = "techPass"
        ps.toByteArray().let {
            System.out.println(Base64.encodeToString(it, Base64.DEFAULT))
        }

    }

    interface Base {
        fun printMessage()
        fun printMessageLine()
    }

    class BaseImpl(val x: Int) : Base {
        override fun printMessage() { print(x) }
        override fun printMessageLine() { print(x) }
    }

    class Derived(p: Base) : Base by p {
        override fun printMessage() { print("absdfc") }
    }

    @Test
    fun main() {
        val b = BaseImpl(1000)
        //Derived(b).printMessage()
        Derived(b).printMessageLine()

    /*    println(lazyValue)
        println(lazyValue)*/

    }
    val lazyValue: String by lazy {
        println("computed!")
        "Hello"
    }

}
