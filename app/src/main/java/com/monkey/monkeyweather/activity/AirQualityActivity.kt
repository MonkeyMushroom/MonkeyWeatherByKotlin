package com.monkey.monkeyweather.activity

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jaeger.library.StatusBarUtil
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.api.Api
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.util.ToastUtil
import com.monkey.monkeyweather.widget.PtrHeader
import kotlinx.android.synthetic.main.activity_air_quality.*

/**
 * 空气质量详情页
 */
class AirQualityActivity : AppCompatActivity(), View.OnClickListener {

    private var mAddress = ""
    private var mCity = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_air_quality)
        StatusBarUtil.setColor(this, resources.getColor(R.color.colorPrimaryDark), 0)
        mAddress = intent.getStringExtra(MainActivity.ADDRESS)
        mCity = intent.getStringExtra(MainActivity.CITY)
        back_iv.setOnClickListener(this)
        title_tv.text = mAddress
        val ptrHeader = PtrHeader(this)
        refresh_layout.headerView = ptrHeader
        refresh_layout.addPtrUIHandler(ptrHeader)
        refresh_layout.setPtrHandler(OnPullDownToRefreshListener())
        Api.getNowAir(this, mCity, OnNowAirRequestListener())
    }

    private inner class OnPullDownToRefreshListener : PtrDefaultHandler() {
        override fun onRefreshBegin(frame: PtrFrameLayout) {
            Api.getNowAir(this@AirQualityActivity, mCity, OnNowAirRequestListener())
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
                air_qlty_view.setAirQuality(nowCity.aqi.toInt())
            } else {
                ToastUtil.show(this@AirQualityActivity, air.status)
            }
        }

        override fun onComplete() {
            super.onComplete()
            refresh_layout.refreshComplete()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_iv -> finish()
        }
    }
}