package com.monkey.monkeyweather.activity

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.jaeger.library.StatusBarUtil
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.adapter.ThreeForecastAdapter
import com.monkey.monkeyweather.api.Api
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.ForecastBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.bean.NowWeatherBean
import com.monkey.monkeyweather.util.ToastUtil
import com.monkey.monkeyweather.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.setTranslucentForImageView(this, 0, null)
        refresh_layout.setPtrHandler(OnPullDownToRefreshListener())
        location_tv.text = "朝阳区 胜古中路"
        Api.getNowWeather(this, OnNowWeatherRequestListener())
        Api.getNowAir(this, OnNowAirRequestListener())
        Api.getWeatherForecast(this, OnWeatherForecastRequestListener())
        forecast_rv.isNestedScrollingEnabled = false
        forecast_rv.layoutManager = LinearLayoutManager(this)
        forecast_rv.addItemDecoration(DividerItemDecoration(this))
    }

    /**
     * 实况天气
     */
    inner class OnNowWeatherRequestListener : Api.OnNetworkRequestListenerAdapter<BaseBean<List<NowWeatherBean>>>() {
        override fun onSuccess(result: BaseBean<List<NowWeatherBean>>) {
            super.onSuccess(result)
            val weather = result.HeWeather6[0]
            if ("ok" == weather.status) {
                val now = weather.now
                temp_tv.text = now.tmp
                weather_tv.text = now.cond_txt
                wind_dir_tv.text = now.wind_dir
                wind_sc_tv.text = now.wind_sc + "级"
                hum_data_tv.text = now.hum + "%"
                fl_data_tv.text = now.fl + "℃"
            } else {
                ToastUtil.show(this@MainActivity, weather.status)
            }
        }

        override fun onComplete() {
            super.onComplete()
            refresh_layout.refreshComplete()
        }
    }

    /**
     * 空气质量实况
     */
    inner class OnNowAirRequestListener : Api.OnNetworkRequestListenerAdapter<BaseBean<List<NowAirBean>>>() {
        override fun onSuccess(result: BaseBean<List<NowAirBean>>) {
            super.onSuccess(result)
            val air = result.HeWeather6[0]
            if ("ok" == air.status) {
                val nowCity = air.air_now_city
                air_qlty_tv.text = "空气" + nowCity.qlty
                aqi_tv.text = nowCity.aqi + " >"
            } else {
                ToastUtil.show(this@MainActivity, air.status)
            }
        }

        override fun onComplete() {
            super.onComplete()
            refresh_layout.refreshComplete()
        }
    }

    /**
     * 3-10天天气预报
     */
    inner class OnWeatherForecastRequestListener : Api.OnNetworkRequestListenerAdapter<BaseBean<List<ForecastBean>>>() {
        override fun onSuccess(result: BaseBean<List<ForecastBean>>) {
            super.onSuccess(result)
            val forecast = result.HeWeather6[0]
            if ("ok" == forecast.status) {
                forecast_rv.adapter = ThreeForecastAdapter(forecast.daily_forecast)
            } else {
                ToastUtil.show(this@MainActivity, forecast.status)
            }
        }

        override fun onComplete() {
            super.onComplete()
            refresh_layout.refreshComplete()
        }
    }

    /**
     * 下拉刷新
     */
    private inner class OnPullDownToRefreshListener : PtrDefaultHandler() {
        override fun onRefreshBegin(frame: PtrFrameLayout) {
            Api.getNowWeather(this@MainActivity, OnNowWeatherRequestListener())
            Api.getNowAir(this@MainActivity, OnNowAirRequestListener())
            Api.getWeatherForecast(this@MainActivity, OnWeatherForecastRequestListener())
        }
    }
}
