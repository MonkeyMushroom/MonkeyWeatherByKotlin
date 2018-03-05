package com.monkey.monkeyweather.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
        window.decorView.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(input_et, InputMethodManager.SHOW_FORCED) //打开软键盘
        }, 200)
        getCityList()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back_iv -> {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(input_et.windowToken, 0) //隐藏软键盘
                finish()
            }
            R.id.clear_iv -> input_et.text = null
        }
    }

    private fun getCityList() {
        var inputStream: InputStream? = null
        try {
            inputStream = assets.open("CountryList.json")
            val size = inputStream.available()
            val bytes = ByteArray(size)
            inputStream.read(bytes)
            val jsonStr = String(bytes)
            val countryList = Gson().fromJson<List<LocalCountryBean>>(jsonStr,
                    object : TypeToken<List<LocalCountryBean>>() {}.type)
            LogUtil.e("--> $countryList")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream!!.close()
        }
    }
}