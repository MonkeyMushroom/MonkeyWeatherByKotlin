package com.monkey.monkeyweather.bean

/**
 * Created by Administrator on 2018/2/27.
 */
data class WeatherBean(var basic: BasicBean,
                       var status: String,
                       var update: UpdateBean,
                       var daily_forecast: List<ForecastBean.DailyForecastBean>,
                       var now: NowWeatherBean.NowBean,
                       var hourly: HourlyBean,
                       var lifestyle: List<LifestyleBean>) {
    data class HourlyBean(
            var time: String,//预报时间，格式yyyy-MM-dd hh:mm
            var tmp: String,//温度
            var cond_code: String, //天气状况代码
            var cond_txt: String, //天气状况代码
            var wind_deg: String, //风向360角度
            var wind_dir: String, //风向
            var wind_sc: String,//风力
            var wind_spd: String,//风速，公里/小时
            var hum: String,//相对湿度
            var pres: String, //大气压强
            var dew: String,//露点温度
            var cloud: String //云量
    )

    data class LifestyleBean(
            var brf: String,//生活指数简介
            var txt: String,//生活指数详细描述
            var type: String//生活指数类型 comf：舒适度指数、cw：洗车指数、drsg：穿衣指数、flu：感冒指数、
            // sport：运动指数、trav：旅游指数、uv：紫外线指数、air：空气污染扩散条件指数
    )
}