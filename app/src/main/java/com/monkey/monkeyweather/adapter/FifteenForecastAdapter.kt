package com.monkey.monkeyweather.adapter

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.bean.ForecastBean.DailyForecastBean
import com.monkey.monkeyweather.util.CalendarUtil
import com.monkey.monkeyweather.util.Constant
import com.monkey.monkeyweather.util.ScreenUtil
import com.monkey.monkeyweather.widget.FifteenForecastTemperatureChartView

/**
 * 15天趋势预报adapter
 */
class FifteenForecastAdapter(data: List<DailyForecastBean>, context: Context, selectPos: Int) :
        BaseQuickAdapter<DailyForecastBean, BaseViewHolder>(R.layout.adapter_fifteen_forecast, data) {

    private val mMaxTempList: ArrayList<Int> = ArrayList()
    private val mMinTempList: ArrayList<Int> = ArrayList()
    private val mRootWidth: Int
    private val mSelectPos = selectPos

    init {
        //获取数据源最高温度和最低温度的集合并排序
        for (forecastBean in data) {
            mMaxTempList.add(forecastBean.tmp_max.toInt())
            mMinTempList.add(forecastBean.tmp_min.toInt())
        }
        mMaxTempList.sort()
        mMinTempList.sort()
        //设置跟布局宽度为屏幕1/5
        mRootWidth = ScreenUtil.getScreenWidth(context) / 5
    }

    override fun convert(helper: BaseViewHolder, item: DailyForecastBean) {
        val curPos = helper.adapterPosition
        val rootLl = helper.getView<LinearLayout>(R.id.root_ll)
        rootLl.layoutParams.width = mRootWidth
        if (mSelectPos == curPos) {//选中的变灰
            rootLl.setBackgroundColor(Color.parseColor("#F7F7F7"))
            helper.setTextColor(R.id.week_tv, mContext.resources.getColor(R.color.colorPrimaryDark))
        }

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
            CalendarUtil.getYesterday() -> helper.setText(R.id.week_tv, "昨天")
            CalendarUtil.getToday() -> helper.setText(R.id.week_tv, "今天")
            CalendarUtil.getTomorrow() -> helper.setText(R.id.week_tv, "明天")
            else -> helper.setText(R.id.week_tv, CalendarUtil.getWeek(item.date))
        }

        val tempChartView = helper.getView<FifteenForecastTemperatureChartView>(R.id.temp_chart_view)
        tempChartView.setFifteenTempRange(mMinTempList[0], mMaxTempList[mMaxTempList.size - 1])
        var yesterdayMaxTemp: Int? = null
        var yesterdayMinTemp: Int? = null
        if (curPos > 0) {
            yesterdayMaxTemp = data[curPos - 1].tmp_max.toInt()
            yesterdayMinTemp = data[curPos - 1].tmp_min.toInt()
        }
        var tomorrowMaxTemp: Int? = null
        var tomorrowMinTemp: Int? = null
        if (curPos < data.size - 1) {
            tomorrowMaxTemp = data[curPos + 1].tmp_max.toInt()
            tomorrowMinTemp = data[curPos + 1].tmp_min.toInt()
        }
        tempChartView.setCurrentTemp(yesterdayMaxTemp, yesterdayMinTemp, item.tmp_max.toInt()
                , item.tmp_min.toInt(), tomorrowMaxTemp, tomorrowMinTemp)
    }
}
