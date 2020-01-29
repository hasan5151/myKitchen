package com.yaros.kitchen.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object{
        fun getHourandMinute(date: Long?): String {
            val pattern = "HH:mm"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            return simpleDateFormat.format(date)
        }

        private fun getMinuteSecond(date: Long?): String {
            val pattern = "mm:ss"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            val date = simpleDateFormat.format(date)
            return date
        }

        fun syncTime(serverDiff : Long) = System.currentTimeMillis()+serverDiff

        fun cookTime(orderTime: Long, cookTime: Long, serverDiff: Long) : Long{ //prevent negative countdown
            if (orderTime+(cookTime*1000)- syncTime(serverDiff)>1001){
                return orderTime+(cookTime*1000)- syncTime(serverDiff)
            }else return 1001
        }

        fun cookTimeDate(remainTime: Long): String{
            val calendar: Calendar = getTodaysCalendar()
            val remainLong = calendar.timeInMillis+remainTime
            return getMinuteSecond(remainLong)
        }

        private fun getTodaysCalendar(): Calendar {
            val cal = Calendar.getInstance()
            val todaysCalendar = Calendar.getInstance()
            todaysCalendar[Calendar.YEAR] = cal[Calendar.YEAR]
            todaysCalendar[Calendar.MONTH] = cal[Calendar.MONTH]
            todaysCalendar[Calendar.DAY_OF_MONTH] = cal[Calendar.DAY_OF_MONTH]
            todaysCalendar[Calendar.HOUR] = 0
            todaysCalendar[Calendar.MINUTE] = 0
            todaysCalendar[Calendar.SECOND] = 0
            todaysCalendar[Calendar.MILLISECOND] = 0
            return todaysCalendar
        }

    }
}