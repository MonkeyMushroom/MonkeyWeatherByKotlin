package com.monkey.monkeyweather.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.bean.ForecastBean.DailyForecastBean
import com.monkey.monkeyweather.util.CalendarUtil
import com.monkey.monkeyweather.util.Constant

/**
 * 三天天气预报adapter
 */
class ThreeForecastAdapter(data: List<DailyForecastBean>) :
        BaseQuickAdapter<DailyForecastBean, BaseViewHolder>(R.layout.adapter_three_forecast, data) {

    override fun convert(helper: BaseViewHolder, item: DailyForecastBean) {
        val weatherTv = helper.getView<TextView>(R.id.weather_tv)
        weatherTv.text = item.cond_txt_d
        weatherTv.setCompoundDrawablesWithIntrinsicBounds(Constant.getWeatherIcon(item.cond_code_d), 0, 0, 0)
        helper.setText(R.id.temp_tv, item.tmp_min + " / " + item.tmp_max + "℃")
        when (item.date) {
            CalendarUtil.getYesterday() -> helper.setText(R.id.date_tv, "昨天")
            CalendarUtil.getToday() -> helper.setText(R.id.date_tv, "今天")
            CalendarUtil.getTomorrow() -> helper.setText(R.id.date_tv, "明天")
            else -> helper.setText(R.id.date_tv, CalendarUtil.getWeek(item.date))
        }
    }
}
