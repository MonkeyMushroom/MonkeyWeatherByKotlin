package com.monkey.monkeyweather.fragment


import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.activity.FifteenForecastActivity
import com.monkey.monkeyweather.activity.MainActivity
import com.monkey.monkeyweather.adapter.LifeStyleAdapter
import com.monkey.monkeyweather.adapter.ThreeForecastAdapter
import com.monkey.monkeyweather.api.Api
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.bean.WeatherBean
import com.monkey.monkeyweather.util.ScreenUtil
import com.monkey.monkeyweather.util.ToastUtil
import com.monkey.monkeyweather.widget.DividerItemDecoration
import com.monkey.monkeyweather.widget.PtrHeader
import kotlinx.android.synthetic.main.fragment_city.*


/**
 * A simple [Fragment] subclass.
 */
class CityFragment : Fragment(), NestedScrollView.OnScrollChangeListener, View.OnClickListener {

    private var mLocation: String = ""
    private var mAddress: String = ""
    private var mCity: String = ""

    companion object {
        const val LOCATION = "LOCATION"
        const val ADDRESS = "ADDRESS"
        const val CITY = "CITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocation = arguments[LOCATION] as String
        mAddress = arguments[ADDRESS] as String
        mCity = arguments[CITY] as String
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_city, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh_layout.setPtrHandler(OnPullDownToRefreshListener())
        val ptrHeader = PtrHeader(activity)
        refresh_layout.headerView = ptrHeader
        refresh_layout.addPtrUIHandler(ptrHeader)
        three_forecast_rv.isNestedScrollingEnabled = false
        three_forecast_rv.layoutManager = LinearLayoutManager(activity)
        three_forecast_rv.addItemDecoration(DividerItemDecoration(activity))
        fifteen_forecast_tv.setOnClickListener(this)
        lifestyle_rv.isNestedScrollingEnabled = false
        lifestyle_rv.layoutManager = LinearLayoutManager(activity)
        lifestyle_rv.addItemDecoration(DividerItemDecoration(activity))

        title_ll.alpha = 0f
        status_view.layoutParams.height = ScreenUtil.getStatusBarHeight(activity)
        scroller.setOnScrollChangeListener(this)

        address_tv.text = mAddress
        Api.getWeather(activity, mLocation, OnWeatherRequestListener())
        Api.getNowAir(activity, mCity, OnNowAirRequestListener())
    }

    /**
     * 标题栏渐变
     */
    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        val titleHeight = resources.getDimensionPixelSize(R.dimen.base_title_height) + ScreenUtil.getStatusBarHeight(activity)
        when {
            scrollY < wind_ll.top -> title_ll.alpha = 0f//透明
            scrollY in wind_ll.top..(three_forecast_rv.top - titleHeight) -> {//渐变
                val percent = (scrollY - wind_ll.top) * 100 / (three_forecast_rv.top - wind_ll.top - titleHeight)
                title_ll.alpha = percent.toFloat() / 100
            }
            else -> title_ll.alpha = 1f//不透明
        }
    }

    /**
     * 下拉刷新
     */
    private inner class OnPullDownToRefreshListener : PtrDefaultHandler() {
        override fun onRefreshBegin(frame: PtrFrameLayout) {
            Api.getWeather(activity, mLocation, OnWeatherRequestListener())
            Api.getNowAir(activity, mCity, OnNowAirRequestListener())
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
                title_tv.text = "$mAddress ${now.tmp}℃"
                temp_tv.text = now.tmp
                weather_tv.text = now.cond_txt
                wind_dir_tv.text = now.wind_dir
                wind_sc_tv.text = now.wind_sc + "级"
                hum_data_tv.text = now.hum + "%"
                fl_data_tv.text = now.fl + "℃"

                val forecastAdapter = ThreeForecastAdapter(weather.daily_forecast)
                forecastAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener(
                        { _: BaseQuickAdapter<Any, BaseViewHolder>, _: View, i: Int ->
                            val intent = Intent(activity, FifteenForecastActivity::class.java)
                            intent.putExtra(MainActivity.LOCATION, mLocation)
                            intent.putExtra(FifteenForecastActivity.SELECT_POSITION, i)
                            startActivity(intent)
                        })
                three_forecast_rv.adapter = forecastAdapter

                val lifeStyleAdapter = LifeStyleAdapter(weather.lifestyle)
                val lifeStyleHeader = View.inflate(activity, R.layout.header_life_style, null)
                lifeStyleAdapter.setHeaderView(lifeStyleHeader)
                lifestyle_rv.adapter = lifeStyleAdapter
            } else {
                ToastUtil.show(activity, weather.status)
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
                air_qlty_tv.text = " | " + nowCity.qlty
                aqi_tv.text = " " + nowCity.aqi + " >"
            } else {
                ToastUtil.show(activity, air.status)
            }
        }

        override fun onComplete() {
            super.onComplete()
            refresh_layout.refreshComplete()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fifteen_forecast_tv -> {
                val intent = Intent(activity, FifteenForecastActivity::class.java)
                intent.putExtra(MainActivity.LOCATION, mLocation)
                intent.putExtra(FifteenForecastActivity.SELECT_POSITION, 0)
                startActivity(intent)
            }
        }
    }
}