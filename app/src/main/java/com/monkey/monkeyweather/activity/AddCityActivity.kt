package com.monkey.monkeyweather.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.adapter.HotCityAdapter
import com.monkey.monkeyweather.bean.LocalCountryBean
import com.monkey.monkeyweather.util.LogUtil
import com.monkey.monkeyweather.util.ToastUtil
import kotlinx.android.synthetic.main.activity_add_city.*
import java.io.IOException
import java.io.InputStream


/**
 * 添加城市
 */
class AddCityActivity : BaseActivity(), View.OnClickListener {

    private val mHotCityArray: Array<String> = arrayOf("定位", "北京", "上海", "广州", "深圳",
            "珠海", "佛山", "南京", "苏州", "厦门", "长沙", "成都", "福州", "杭州", "武汉",
            "青岛", "西安", "太原", "石家庄", "沈阳", "重庆", "天津", "南宁")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city)
        back_iv.setOnClickListener(this)
        clear_iv.setOnClickListener(this)
        window.decorView.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(input_et, InputMethodManager.SHOW_FORCED) //打开软键盘
        }, 200)
        getAllCityList()
        setHotCityList()
    }

    private fun getAllCityList() {
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

    private fun setHotCityList() {
        hot_city_rv.layoutManager = FlexboxLayoutManager()
        val hotCityAdapter = HotCityAdapter(mHotCityArray.toList())
        hotCityAdapter.setOnItemChildClickListener({ _, _, position ->
            ToastUtil.show(this@AddCityActivity, mHotCityArray[position])
        })
        hot_city_rv.adapter = hotCityAdapter
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
}