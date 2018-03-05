package com.monkey.monkeyweather.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.adapter.FifteenForecastAdapter
import com.monkey.monkeyweather.api.Api
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.ForecastBean
import com.monkey.monkeyweather.util.ToastUtil
import kotlinx.android.synthetic.main.activity_fifteen_forecast.*

/**
 * 15天趋势预报
 */
class FifteenForecastActivity : AppCompatActivity(), View.OnClickListener {

    private var mLocation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifteen_forecast)
        close_iv.setOnClickListener(this)
        fifteen_forecast_rv.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false)
        mLocation = intent.getStringExtra(MainActivity.LOCATION)
        Api.getWeatherForecast(this, mLocation, OnWeatherForecastRequestListener())
    }

    /**
     * 3-10天天气预报
     */
    inner class OnWeatherForecastRequestListener : Api.OnNetworkRequestListenerAdapter<BaseBean<List<ForecastBean>>>() {
        override fun onSuccess(result: BaseBean<List<ForecastBean>>) {
            super.onSuccess(result)
            val forecast = result.HeWeather6[0]
            if ("ok" == forecast.status) {
                fifteen_forecast_rv.adapter = FifteenForecastAdapter(forecast.daily_forecast)
            } else {
                ToastUtil.show(this@FifteenForecastActivity, forecast.status)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.close_iv -> finish()
        }
    }
}
