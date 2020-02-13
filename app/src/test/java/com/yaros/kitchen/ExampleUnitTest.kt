package com.yaros.kitchen

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yaros.kitchen.models.StopListModel
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.utils.DateUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.reflect.Type
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
        print(DateUtil.calculateCookingTime("1580710908000",1580800547000))
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

    @Test
    fun testGroupBy(){
        val x= PrintersModel("1","sdf",false)
        val x1= PrintersModel("2","sd4",false)
        val x2= PrintersModel("1","2sd",false)
        val x3 = PrintersModel("2","s2d",false)
        val x4 =PrintersModel("2","sd2",false)
        val x5= PrintersModel("2","s1d",false)

        listOf(x,x1,x2,x3,x4,x5)
            .groupBy { it.id }.forEach({
                System.out.println(it.key)


                it.value.forEach {
                    System.out.println(it.name)
                }


        })


    }

    @Test
    fun testDrop(){
        var  x = listOf(1,2,3,4,5,6,7)
//        x=  x.dropLast(x.size-2)
      x.dropLast(x.size-2)
         System.out.println(x)
    }


    @Test
    fun removeAt(){
        val x : List<StopListModel> = listOf(
            StopListModel("Стейк из семги с ризотто","x1",false),
            StopListModel("Манты с мясом","x5",false),
            StopListModel("жаренная чучвара","x2",false),
            StopListModel("Стейк из семги с овощами","x4",false)
        )
        System.out.println( "${x.size}")
    }

    @Test
    fun testListtoJson(){
        val x : List<StopListModel> = listOf(
            StopListModel("Стейк из семги с ризотто","x1",false),
            StopListModel("Манты с мясом","x5",false),
            StopListModel("жаренная чучвара","x2",false),
            StopListModel("Стейк из семги с овощами","x4",false)
        )



        Gson().toJson(x).let {
            System.out.println(it)

            val listType: Type = object : TypeToken<ArrayList<StopListModel?>?>() {}.getType()
            val yourClassList: List<StopListModel> = Gson().fromJson(it, listType)

            System.out.println(yourClassList.size)

        }



     }

    @Test
    fun testForeach(){
        val x = listOf(2,4,6,8,31)

        x.forEachIndexed{index,unit->

            if (x.size-1==index){
                System.out.println("this is last item")
            }

            System.out.println(" text index  ${x.size} ${index} ${unit}")

        }

    }
}
