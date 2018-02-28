package com.monkey.monkeyweather.activity

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jaeger.library.StatusBarUtil
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.adapter.LifeStyleAdapter
import com.monkey.monkeyweather.adapter.ThreeForecastAdapter
import com.monkey.monkeyweather.api.Api
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.ForecastBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.bean.WeatherBean
import com.monkey.monkeyweather.util.ScreenUtil
import com.monkey.monkeyweather.util.ToastUtil
import com.monkey.monkeyweather.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NestedScrollView.OnScrollChangeListener, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.setTranslucentForImageView(this, 0, null)
        refresh_layout.setPtrHandler(OnPullDownToRefreshListener())
        location_tv.text = "朝阳区 胜古中路"
        Api.getWeather(this, OnWeatherRequestListener())
        Api.getNowAir(this, OnNowAirRequestListener())
//        Api.getWeatherForecast(this, OnWeatherForecastRequestListener())
        forecast_rv.isNestedScrollingEnabled = false
        forecast_rv.layoutManager = LinearLayoutManager(this)
        forecast_rv.addItemDecoration(DividerItemDecoration(this))
        lifestyle_rv.isNestedScrollingEnabled = false
        lifestyle_rv.layoutManager = LinearLayoutManager(this)
        lifestyle_rv.addItemDecoration(DividerItemDecoration(this))

        title_ll.alpha = 0f
        status_view.layoutParams.height = ScreenUtil.getStatusBarHeight(this)
        scroller.setOnScrollChangeListener(this)
        add_iv.setOnClickListener(this)
        more_iv.setOnClickListener(this)
    }

    /**
     * 标题栏渐变
     */
    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        when {
            scrollY < wind_ll.top -> title_ll.alpha = 0f//透明
            scrollY in wind_ll.top..forecast_rv.top -> {//渐变
                val percent = (scrollY - wind_ll.top) * 100 / (forecast_rv.top - wind_ll.top)
                title_ll.alpha = percent.toFloat() / 100
            }
            else -> title_ll.alpha = 1f//不透明
        }
    }

    /**
     * 常规天气集合
     * 3-7天天气预报、实况天气、逐小时预报以及生活指数
     */
    inner class OnWeatherRequestListener : Api.OnNetworkRequestListenerAdapter<BaseBean<List<WeatherBean>>>() {
        override fun onSuccess(result: BaseBean<List<WeatherBean>>) {
            super.onSuccess(result)
            val weather = result.HeWeather6[0]
            if ("ok" == weather.status) {
                val now = weather.now
                title_tv.text = "朝阳区 胜古中路 ${now.tmp}℃"
                temp_tv.text = now.tmp
                weather_tv.text = now.cond_txt
                wind_dir_tv.text = now.wind_dir
                wind_sc_tv.text = now.wind_sc + "级"
                hum_data_tv.text = now.hum + "%"
                fl_data_tv.text = now.fl + "℃"

                forecast_rv.adapter = ThreeForecastAdapter(weather.daily_forecast)
                val lifeStyleAdapter = LifeStyleAdapter(weather.lifestyle)
                val lifeStyleHeader = View.inflate(this@MainActivity, R.layout.header_life_style, null)
                lifeStyleAdapter.setHeaderView(lifeStyleHeader)
                lifestyle_rv.adapter = lifeStyleAdapter
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
                air_qlty_tv.text = nowCity.qlty
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
            Api.getWeather(this@MainActivity, OnWeatherRequestListener())
            Api.getNowAir(this@MainActivity, OnNowAirRequestListener())
//            Api.getWeatherForecast(this@MainActivity, OnWeatherForecastRequestListener())
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_iv -> ToastUtil.show(this, "add")
            R.id.more_iv -> ToastUtil.show(this, "more")
        }
    }
}
