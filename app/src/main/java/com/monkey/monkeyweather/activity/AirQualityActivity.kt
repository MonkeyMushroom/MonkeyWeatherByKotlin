package com.monkey.monkeyweather.activity

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import com.jaeger.library.StatusBarUtil
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.api.Api
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.util.ToastUtil
import com.monkey.monkeyweather.widget.AirQualityView
import com.monkey.monkeyweather.widget.PtrHeader
import kotlinx.android.synthetic.main.activity_air_quality.*

/**
 * 空气质量详情页
 */
class AirQualityActivity : BaseActivity(), View.OnClickListener {

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
                val aqiInt = nowCity.aqi.toInt()
                air_qlty_view.setAirQuality(aqiInt)
                air_qlty_tv.text = AirQualityView.getAirQualityText(aqiInt)[0]
                air_qlty_tv.setTextColor(AirQualityView.getAirQualityColorResource(
                        this@AirQualityActivity, aqiInt))
                pub_time_tv.text = nowCity.pub_time + "发布"
                suggest_tv.text = AirQualityView.getAirQualityText(aqiInt)[1]

                PM2_5_value.text = nowCity.pm25
                PM10_value.text = nowCity.pm10
                SO2.text = getSpannableString("SO2")
                SO2_value.text = nowCity.so2
                NO2.text = getSpannableString("NO2")
                NO2_value.text = nowCity.no2
                CO_value.text = (nowCity.co.toFloat() * 1000).toInt().toString()
                O3.text = getSpannableString("O3")
                O3_value.text = nowCity.o3
            } else {
                ToastUtil.show(this@AirQualityActivity, air.status)
            }
        }

        /**
         * 分段改变字体大小，完成化学式显示
         */
        private fun getSpannableString(content: String): SpannableString {
            val ssb = SpannableString(content)
            ssb.setSpan(AbsoluteSizeSpan(9, true), content.lastIndex,
                    content.lastIndex + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            return ssb
        }

        override fun onComplete() {
            super.onComplete()
            refresh_layout.refreshComplete()
        }

        override fun onError(context: Context, e: Throwable) {
            super.onError(context, e)
            refresh_layout.refreshComplete()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_iv -> finish()
        }
    }
}