package com.monkey.monkeyweather.bean

data class UpdateBean(
        var loc: String,//当地时间，24小时制，格式yyyy-MM-dd HH:mm
        var utc: String //UTC时间，24小时制，格式yyyy-MM-dd HH:mm
)
