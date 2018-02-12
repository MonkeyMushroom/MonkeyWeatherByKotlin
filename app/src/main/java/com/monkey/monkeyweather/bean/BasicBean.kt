package com.monkey.monkeyweather.bean

data class BasicBean(
        var cid: String,//地区／城市名称
        var location: String,//地区／城市ID
        var parent_city: String,//地区／城市纬度
        var admin_area: String,//地区／城市经度
        var cnty: String,//该地区／城市的上级城市
        var lat: String,//该地区／城市所属行政区域
        var lon: String,//该地区／城市所属国家名称
        var tz: String //该地区／城市所在时区
)