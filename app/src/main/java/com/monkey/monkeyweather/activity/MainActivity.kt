package com.monkey.monkeyweather.activity

import android.Manifest
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.jaeger.library.StatusBarUtil
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.fragment.CityFragment
import com.monkey.monkeyweather.util.LogUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_city.*


class MainActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val ADDRESS = "ADDRESS"
        const val CITY = "CITY"
    }

    private var mLocationClient: LocationClient? = null
    private val mLocationListener = LocationListener()
    private var mLocation: BDLocation? = null
    private var longitude: Double? = null
    private var latitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.setTranslucentForImageView(this, 0, null)
//        add_iv.setOnClickListener(this)
        refresh_iv.setOnClickListener(this)
        getLocation()
    }

    private fun getLocation() {
        mLocationClient = LocationClient(applicationContext)
        //注册监听函数
        mLocationClient!!.registerLocationListener(mLocationListener)
        val option = LocationClientOption()
        option.setIsNeedAddress(true)
        mLocationClient!!.locOption = option
        checkRuntimePermission()
    }

    private fun checkRuntimePermission() {
        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
        checkRuntimePermission(permissions, object : BaseActivity.RuntimePermissionListener {
            override fun onRuntimePermissionGranted() {
                mLocationClient!!.start()
            }

            override fun onRuntimePermissionDenied() {
                val snakeBar = Snackbar.make(root_rl, getString(R.string.permission_denied), Snackbar.LENGTH_LONG)
                        .setAction("获取权限", {
                            checkRuntimePermission()
                        })
                        .setActionTextColor(resources.getColor(R.color.base_white))
                snakeBar.view.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                snakeBar.show()
            }
        })
    }

    inner class LocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            mLocation = location
            mLocationClient!!.stop()
            longitude = location.longitude //获取经度信息
            latitude = location.latitude //获取纬度信息
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
            if (city_pager.adapter == null) {
                city_pager.adapter = CityPagerAdapter()
            } else {
                val fragment = supportFragmentManager.fragments[city_pager.currentItem] as CityFragment
                fragment.setLocationData("${mLocation!!.district} ${mLocation!!.street}", mLocation!!.city)
                fragment.scroller.scrollTo(0, 0)
                fragment.refresh_layout.autoRefresh()
            }
        }
    }

    inner class CityPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {
        override fun getCount(): Int {
            return 1
        }

        override fun getItem(position: Int): Fragment {
            val fragment = CityFragment()
            val bundle = Bundle()
            bundle.putString(ADDRESS, "${mLocation!!.district} ${mLocation!!.street}")
            bundle.putString(CITY, mLocation!!.city)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
//            R.id.add_iv ->
//                startActivity(Intent(this@MainActivity, AddCityActivity::class.java))
            R.id.refresh_iv ->
                getLocation()
        }
    }
}
