package com.yaros.kitchen

import android.util.Base64
import com.yaros.kitchen.utils.DateUtil
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

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

    @Test
    fun dateTest(){
        val pattern = "HH:mm"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(Date())
        println(date)
    }

    @Test
    fun testTimeStamp(){
        print(System.currentTimeMillis())
    }

    @Test
    fun testGetHour(){
        print(DateUtil.getHourandMinute(20*60*1000000))
    }

    @Test
    fun testCookTimeDate(){
        print(DateUtil.cookTimeDate(20*60*1000))
    }

    @Test
    fun calculateCookingTime(){
        print(DateUtil.calculateCookingTime("1580710908000"))
    }


    @Test
    fun testCookTime(){
        print(DateUtil.remainCookTime(System.currentTimeMillis(),20,0))
//        print(DateUtil.cookTime(1580292031307,20*60,0))
    }
    @Test
    fun testGuid(){
        print(UUID.randomUUID().toString())
//        print(DateUtil.cookTime(1580292031307,20*60,0))
    }

}
