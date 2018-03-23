package com.monkey.monkeyweather.api

import com.monkey.monkeyweather.bean.*
import com.monkey.monkeyweather.util.Constant
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap


/**
 * api接口
 */
interface ApiService {

    @POST(Constant.AIR_NOW)
    fun getNowAir(@Query("location") location: String, @QueryMap params: HashMap<String, String>)
            : Observable<BaseBean<List<NowAirBean>>>

    @POST(Constant.WEATHER_FORECAST)
    fun getWeatherForecast(@Query("location") location: String, @QueryMap params: HashMap<String, String>)
            : Observable<BaseBean<List<ForecastBean>>>

    @POST(Constant.WEATHER)
    fun getWeather(@Query("location") location: String, @QueryMap params: HashMap<String, String>)
            : Observable<BaseBean<List<WeatherBean>>>

    @POST(Constant.SUNRISE_SUNSET)
    fun getSunriseSunset(@Query("location") location: String, @QueryMap params: HashMap<String, String>)
            : Observable<BaseBean<List<SunriseSunsetBean>>>

}