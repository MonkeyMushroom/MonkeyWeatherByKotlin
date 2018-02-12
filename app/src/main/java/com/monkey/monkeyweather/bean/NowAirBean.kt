package com.monkey.monkeyweather.bean

/**
 * 空气质量实况
 */
data class NowAirBean(var basic: BasicBean,
                      var status: String,
                      var update: UpdateBean,
                      var air_now_city: AirNowCityBean,
                      var air_now_station: List<AirNowStationBean>) {
    data class AirNowCityBean(//城市实况
            var aqi: String,//空气质量指数
            var co: String,//一氧化碳
            var main: String,//主要污染物
            var no2: String,//二氧化氮
            var o3: String,//臭氧
            var pm10: String,//pm10
            var pm25: String,//pm25
            var pub_time: String,//数据发布时间,格式yyyy-MM-dd HH:mm
            var qlty: String,//空气质量，取值范围:优，良，轻度污染，中度污染，重度污染，严重污染
            var so2: String//二氧化硫
    )

    data class AirNowStationBean(//城市实况
            var air_sta: String,//站点名称
            var asid: String,//站点ID
            var lat: String,//站点纬度
            var lon: String,//站点经度
            var aqi: String,//空气质量指数
            var co: String,//一氧化碳
            var main: String,//主要污染物
            var no2: String,//二氧化氮
            var o3: String,//臭氧
            var pm10: String,//pm10
            var pm25: String,//pm25
            var pub_time: String,//数据发布时间,格式yyyy-MM-dd HH:mm
            var qlty: String,//空气质量，取值范围:优，良，轻度污染，中度污染，重度污染，严重污染
            var so2: String//二氧化硫
    )
}
