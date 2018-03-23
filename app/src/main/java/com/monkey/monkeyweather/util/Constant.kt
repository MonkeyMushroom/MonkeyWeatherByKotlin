package com.monkey.monkeyweather.util

import com.monkey.monkeyweather.R

/**
 * 配置类
 */
object Constant {
    const val BASE_URL = "https://free-api.heweather.com/"
    const val USERNAME = "HE1802011039311694"
    const val KEY = "42875c8c480a45f1ac9d457c5afae338"

    const val WEATHER = "s6/weather"
    const val WEATHER_FORECAST = "s6/weather/forecast"
    const val AIR_NOW = "s6/air/now"
    const val SUNRISE_SUNSET = "s6/solar/sunrise-sunset"

    /**
     * 天气代码和资源的映射关系
     */
    fun getWeatherIcon(code: String): Int {
        when (code) {
            "100" -> return R.drawable.c100
            "101" -> return R.drawable.c101
            "102" -> return R.drawable.c102
            "103" -> return R.drawable.c103
            "104" -> return R.drawable.c104
            "200" -> return R.drawable.c200
            "201" -> return R.drawable.c201
            "202" -> return R.drawable.c202
            "203" -> return R.drawable.c203
            "204" -> return R.drawable.c204
            "205" -> return R.drawable.c205
            "206" -> return R.drawable.c206
            "207" -> return R.drawable.c207
            "208" -> return R.drawable.c208
            "209" -> return R.drawable.c209
            "210" -> return R.drawable.c210
            "211" -> return R.drawable.c211
            "212" -> return R.drawable.c212
            "213" -> return R.drawable.c213
            "300" -> return R.drawable.c300
            "301" -> return R.drawable.c301
            "302" -> return R.drawable.c302
            "303" -> return R.drawable.c303
            "304" -> return R.drawable.c304
            "305" -> return R.drawable.c305
            "306" -> return R.drawable.c306
            "307" -> return R.drawable.c307
            "308" -> return R.drawable.c312
            "309" -> return R.drawable.c309
            "310" -> return R.drawable.c310
            "311" -> return R.drawable.c311
            "312" -> return R.drawable.c312
            "313" -> return R.drawable.c313
            "400" -> return R.drawable.c400
            "401" -> return R.drawable.c401
            "402" -> return R.drawable.c402
            "403" -> return R.drawable.c403
            "404" -> return R.drawable.c404
            "405" -> return R.drawable.c405
            "406" -> return R.drawable.c406
            "407" -> return R.drawable.c407
            "500" -> return R.drawable.c500
            "501" -> return R.drawable.c501
            "502" -> return R.drawable.c502
            "503" -> return R.drawable.c503
            "504" -> return R.drawable.c504
            "507" -> return R.drawable.c507
            "508" -> return R.drawable.c508
            "900" -> return R.drawable.c900
            "901" -> return R.drawable.c901
            "999" -> return R.drawable.c999
            else -> return R.drawable.c999
        }
    }
}