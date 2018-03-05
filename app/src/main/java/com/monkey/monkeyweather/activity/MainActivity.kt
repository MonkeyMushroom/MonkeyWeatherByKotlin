package com.monkey.monkeyweather.activity

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jaeger.library.StatusBarUtil
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.adapter.LifeStyleAdapter
import com.monkey.monkeyweather.adapter.ThreeForecastAdapter
import com.monkey.monkeyweather.api.Api
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.ForecastBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.bean.WeatherBean
import com.monkey.monkeyweather.util.LogUtil
import com.monkey.monkeyweather.util.ScreenUtil
import com.monkey.monkeyweather.util.ToastUtil
import com.monkey.monkeyweather.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), NestedScrollView.OnScrollChangeListener, View.OnClickListener {

    companion object {
        const val LOCATION = "LOCATION"
    }

    private var mLocationClient: LocationClient? = null
    private val mLocationListener = LocationListener()
    private var mLocation: String = ""//位置（经纬度）

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.setTranslucentForImageView(this, 0, null)
        refresh_layout.setPtrHandler(OnPullDownToRefreshListener())
        three_forecast_rv.isNestedScrollingEnabled = false
        three_forecast_rv.layoutManager = LinearLayoutManager(this)
        three_forecast_rv.addItemDecoration(DividerItemDecoration(this))
        fifteen_forecast_tv.setOnClickListener(this)
        lifestyle_rv.isNestedScrollingEnabled = false
        lifestyle_rv.layoutManager = LinearLayoutManager(this)
        lifestyle_rv.addItemDecoration(DividerItemDecoration(this))

        title_ll.alpha = 0f
        status_view.layoutParams.height = ScreenUtil.getStatusBarHeight(this)
        scroller.setOnScrollChangeListener(this)
        add_iv.setOnClickListener(this)
        more_iv.setOnClickListener(this)

        getLocation()
    }

    private fun getLocation() {
        mLocationClient = LocationClient(applicationContext)
        //注册监听函数
        mLocationClient!!.registerLocationListener(mLocationListener)
        val option = LocationClientOption()
        option.setIsNeedAddress(true)
        mLocationClient!!.locOption = option
        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
        checkRuntimePermission(permissions, object : BaseActivity.RuntimePermissionListener {
            override fun onRuntimePermissionGranted() {
                mLocationClient!!.start()
            }

            override fun onRuntimePermissionDenied() {
                ToastUtil.show(this@MainActivity, "拒绝权限无法精确获取当前位置的天气情况哦O(∩_∩)O")
            }
        })
    }

    inner class LocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {

            val longitude = location.longitude//获取经度信息
            val latitude = location.latitude //获取纬度信息
            mLocation = longitude.toString() + "," + latitude.toString()
            val radius = location.radius    //获取定位精度，默认值为0.0f
            val coorType = location.coorType//获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            val errorCode = location.locType//获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            val addr = location.addrStr    //获取详细地址信息
            val country = location.country    //获取国家
            val province = location.province    //获取省份
            val city = location.city    //获取城市
            val district = location.district    //获取区县
            val street = location.street    //获取街道信息

            LogUtil.e("--> $longitude $latitude $radius $coorType $errorCode\n" +
                    "$addr $country $province $city $district $street")
            mLocationClient!!.stop()
            title_tv.text = "$district $street"
            location_tv.text = "$district $street"
            Api.getWeather(this@MainActivity, mLocation, OnWeatherRequestListener())
            Api.getNowAir(this@MainActivity, city, OnNowAirRequestListener())
        }
    }

    /**
     * 标题栏渐变
     */
    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        when {
            scrollY < wind_ll.top -> title_ll.alpha = 0f//透明
            scrollY in wind_ll.top..three_forecast_rv.top -> {//渐变
                val percent = (scrollY - wind_ll.top) * 100 / (three_forecast_rv.top - wind_ll.top)
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

                val forecastAdapter = ThreeForecastAdapter(weather.daily_forecast)
                forecastAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener(
                        { adapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, i: Int ->
                            val item = adapter.getItem(i) as ForecastBean.DailyForecastBean
                            ToastUtil.show(this@MainActivity, item.date)
                        })
                three_forecast_rv.adapter = forecastAdapter

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
     * 下拉刷新
     */
    private inner class OnPullDownToRefreshListener : PtrDefaultHandler() {
        override fun onRefreshBegin(frame: PtrFrameLayout) {
            mLocationClient!!.start()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fifteen_forecast_tv -> {
                val intent = Intent(this@MainActivity, FifteenForecastActivity::class.java)
                intent.putExtra(LOCATION, mLocation)
                startActivity(intent)
            }
            R.id.add_iv ->
                startActivity(Intent(this@MainActivity, AddCityActivity::class.java))
            R.id.more_iv ->
                ToastUtil.show(this, "more")
        }
    }
}
