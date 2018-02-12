package com.monkey.monkeyweather.api

import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.ForecastBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.bean.NowWeatherBean
import com.monkey.monkeyweather.util.Constant
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * api接口
 */
interface ApiService {

    //https://free-api.heweather.com/s6/weather/now?location=%E5%8C%97%E4%BA%AC&key=42875c8c480a45f1ac9d457c5afae338
    @POST(Constant.WEATHER_NOW)
    fun getNowWeather(@Query("location") location: String, @Query("key") key: String)
            : Observable<BaseBean<List<NowWeatherBean>>>

    @POST(Constant.AIR_NOW)
    fun getNowAir(@Query("location") location: String, @Query("key") key: String)
            : Observable<BaseBean<List<NowAirBean>>>

    @POST(Constant.WEATHER_FORECAST)
    fun getWeatherForecast(@Query("location") location: String, @Query("key") key: String)
            : Observable<BaseBean<List<ForecastBean>>>

}