package com.monkey.monkeyweather.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * 日历工具类
 */
object CalendarUtil {

    private val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getToday(): String {
        return format.format(Date())
    }

    fun getTomorrow(): String {
        val calendar = Calendar.getInstance()
        calendar.set(calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH] + 1)
        return format.format(calendar.time)
    }

    fun getWeek(dateStr: String): String {
        val date = format.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> "周一"
            Calendar.TUESDAY -> "周二"
            Calendar.WEDNESDAY -> "周三"
            Calendar.THURSDAY -> "周四"
            Calendar.FRIDAY -> "周五"
            Calendar.SATURDAY -> "周六"
            Calendar.SUNDAY -> "周天"
            else -> ""
        }
    }
}