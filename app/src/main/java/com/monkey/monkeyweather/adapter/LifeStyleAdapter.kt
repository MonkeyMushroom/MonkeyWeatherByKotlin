package com.monkey.monkeyweather.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.bean.WeatherBean

/**
 * 生活指数adapter
 */
class LifeStyleAdapter(data: List<WeatherBean.LifestyleBean>) :
        BaseQuickAdapter<WeatherBean.LifestyleBean, BaseViewHolder>(R.layout.adapter_life_style, data) {

    override fun convert(helper: BaseViewHolder, item: WeatherBean.LifestyleBean) {
        when (item.type) {
            "comf" -> helper.setBackgroundRes(R.id.type_iv, R.drawable.comf)
                    .setText(R.id.state_tv, "舒适度：" + item.brf)
            "cw" -> helper.setBackgroundRes(R.id.type_iv, R.drawable.cw)
                    .setText(R.id.state_tv, "洗车：" + item.brf)
            "drsg" -> helper.setBackgroundRes(R.id.type_iv, R.drawable.drsg)
                    .setText(R.id.state_tv, "穿衣：" + item.brf)
            "flu" -> helper.setBackgroundRes(R.id.type_iv, R.drawable.flu)
                    .setText(R.id.state_tv, "感冒：" + item.brf)
            "sport" -> helper.setBackgroundRes(R.id.type_iv, R.drawable.sport)
                    .setText(R.id.state_tv, "运动：" + item.brf)
            "trav" -> helper.setBackgroundRes(R.id.type_iv, R.drawable.trav)
                    .setText(R.id.state_tv, "旅游：" + item.brf)
            "uv" -> helper.setBackgroundRes(R.id.type_iv, R.drawable.uv)
                    .setText(R.id.state_tv, "紫外线：" + item.brf)
            "air" -> helper.setBackgroundRes(R.id.type_iv, R.drawable.comf)
                    .setText(R.id.state_tv, "空气污染扩散：" + item.brf)
        }
        helper.setText(R.id.suggest_tv, item.txt)
    }
}
