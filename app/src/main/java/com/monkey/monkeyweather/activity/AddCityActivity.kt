package com.monkey.monkeyweather.activity

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.bean.LocalCountryBean
import com.monkey.monkeyweather.util.LogUtil
import kotlinx.android.synthetic.main.activity_add_city.*
import java.io.IOException
import java.io.InputStream

/**
 * 添加城市
 */
class AddCityActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        back_iv.setOnClickListener(this)
        clear_iv.setOnClickListener(this)
        getCityList()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back_iv -> finish()
            R.id.clear_iv -> input_et.text = null
        }
    }

    private fun getCityList() {
        var inputStream: InputStream? = null
        try {
            inputStream = assets.open("CountryList.json")
            val size = inputStream!!.available()
            val len = -1
            val bytes = ByteArray(size)
            inputStream.read(bytes)
            inputStream.close()
            val jsonStr = String(bytes)
            val countryList = Gson().fromJson<List<LocalCountryBean>>(jsonStr,
                    object : TypeToken<List<LocalCountryBean>>() {}.type)
            LogUtil.e("--> $countryList")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}