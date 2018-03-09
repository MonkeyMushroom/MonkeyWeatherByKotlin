package com.monkey.monkeyweather.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.monkey.monkeyweather.R

/**
 * 热门城市adapter
 */
class HotCityAdapter(data: List<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_hot_city, data) {
    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.setText(R.id.name_tv, item)
                .addOnClickListener(R.id.name_tv)
        val nameTv = helper.getView<TextView>(R.id.name_tv)
        if (helper.adapterPosition == 0) {
            nameTv.compoundDrawablePadding = 4
            nameTv.setTextColor(mContext.resources.getColor(R.color.colorPrimaryDark))
            nameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.location, 0)
        }
    }
}