package com.monkey.monkeyweather.api

import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.ForecastBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.bean.WeatherBean
import com.monkey.monkeyweather.util.Constant
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * api接口
 */
interface ApiService {

    @POST(Constant.AIR_NOW)
    fun getNowAir(@Query("location") location: String, @Query("key") key: String)
            : Observable<BaseBean<List<NowAirBean>>>

    @POST(Constant.WEATHER_FORECAST)
    fun getWeatherForecast(@Query("location") location: String, @Query("key") key: String)
            : Observable<BaseBean<List<ForecastBean>>>

    @POST(Constant.WEATHER)
    fun getWeather(@Query("location") location: String, @Query("key") key: String)
            : Observable<BaseBean<List<WeatherBean>>>

}