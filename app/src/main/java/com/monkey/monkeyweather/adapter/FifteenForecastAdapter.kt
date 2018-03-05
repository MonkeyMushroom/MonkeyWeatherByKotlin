package com.monkey.monkeyweather.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.bean.ForecastBean.DailyForecastBean
import com.monkey.monkeyweather.util.CalendarUtil
import com.monkey.monkeyweather.util.Constant

/**
 * 15天趋势预报adapter
 */
class FifteenForecastAdapter(data: List<DailyForecastBean>) :
        BaseQuickAdapter<DailyForecastBean, BaseViewHolder>(R.layout.adapter_fifteen_forecast, data) {

    override fun convert(helper: BaseViewHolder, item: DailyForecastBean) {
        helper.setText(R.id.date_tv, CalendarUtil.translate(item.date))
                .setText(R.id.day_weather_tv, item.cond_txt_d)
                .setText(R.id.night_weather_tv, item.cond_txt_n)
                .setText(R.id.wind_dir_tv, item.wind_dir)
                .setText(R.id.wind_sc_tv, item.wind_sc + "级")
        val dayWeatherTv = helper.getView<TextView>(R.id.day_weather_tv)
        dayWeatherTv.setCompoundDrawablesWithIntrinsicBounds(0, Constant.getWeatherIcon(item.cond_code_d), 0, 0)
        val nightWeatherTv = helper.getView<TextView>(R.id.night_weather_tv)
        nightWeatherTv.setCompoundDrawablesWithIntrinsicBounds(0, Constant.getWeatherIcon(item.cond_code_n), 0, 0)
        when (item.date) {
            CalendarUtil.getToday() -> helper.setText(R.id.week_tv, "今天")
            CalendarUtil.getTomorrow() -> helper.setText(R.id.week_tv, "明天")
            else -> helper.setText(R.id.week_tv, CalendarUtil.getWeek(item.date))
        }
    }
}
