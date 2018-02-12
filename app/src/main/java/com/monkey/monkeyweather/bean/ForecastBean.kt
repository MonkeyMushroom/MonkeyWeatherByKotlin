package com.monkey.monkeyweather.bean

/**
 * 实况天气
 */
data class ForecastBean(var basic: BasicBean,
                        var status: String,
                        var update: UpdateBean,
                        var daily_forecast: List<DailyForecastBean>) {
    data class DailyForecastBean(
            var date: String, //预报日期    2013-12-30
            var sr: String, //日出时间    07:36
            var ss: String, //日落时间    16:58
            var mr: String, //月升时间    04:47
            var ms: String, //月落时间    14:59
            var tmp_max: String, //最高温度    4
            var tmp_min: String, //最低温度    -5
            var cond_code_d: String, //白天天气状况代码    100
            var cond_code_n: String, //晚间天气状况代码    100
            var cond_txt_d: String, //白天天气状况描述    晴
            var cond_txt_n: String, //晚间天气状况描述    晴
            var wind_deg: String, //风向360角度    310
            var wind_dir: String, //风向    西北风
            var wind_sc: String, //风力    1-2
            var wind_spd: String, //风速，公里/小时    14
            var hum: String, //相对湿度    37
            var pcpn: String, //降水量    0
            var pop: String, //降水概率    0
            var pres: String, //大气压强    1018
            var uv_index: String, //紫外线强度指数    3
            var vis: String   //能见度，单位：公里
    )
}
