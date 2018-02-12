package com.monkey.monkeyweather.bean

/**
 * 实况天气
 */
data class NowWeatherBean(var basic: BasicBean,
                          var status: String,
                          var update: UpdateBean,
                          var now: NowBean) {
    data class NowBean(
            var cond_code: String,//实况天气状况代码
            var cond_txt: String,//实况天气状况代码
            var fl: String,//体感温度，默认单位：摄氏度
            var hum: String,//相对湿度
            var pcpn: String,//降水量
            var pres: String,//大气压强
            var tmp: String,//温度，默认单位：摄氏度
            var vis: String,//能见度，默认单位：公里
            var wind_deg: String,//风向360角度
            var wind_dir: String,//风向
            var wind_sc: String,//风力
            var wind_spd: String //风速，公里/小时
    )
}
