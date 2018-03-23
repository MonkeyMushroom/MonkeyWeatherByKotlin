package com.monkey.monkeyweather.bean

data class SunriseSunsetBean(var basic: BasicBean,
                             var status: String,
                             var update: UpdateBean,
                             var sunrise_sunset: List<SunriseSunsetBean>) {
    data class SunriseSunsetBean(var date: String,
                                 var sr: String,
                                 var ss: String)
}