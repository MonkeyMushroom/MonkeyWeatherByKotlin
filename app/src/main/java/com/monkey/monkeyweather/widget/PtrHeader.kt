package com.monkey.monkeyweather.widget

import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrUIHandler
import `in`.srain.cube.views.ptr.indicator.PtrIndicator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.util.LogUtil
import kotlinx.android.synthetic.main.header_ptr.view.*

/**
 * 自定义下拉刷新header
 */
class PtrHeader constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr), PtrUIHandler {

    init {
        View.inflate(context, R.layout.header_ptr, this)
    }

    override fun onUIRefreshPrepare(frame: PtrFrameLayout) {
        ptr_tv.text = context.getString(R.string.pull_to_refresh)
        LogUtil.e("--> onUIRefreshPrepare")
    }

    override fun onUIRefreshBegin(frame: PtrFrameLayout) {
        ptr_tv.text = context.getString(R.string.refreshing)
        LogUtil.e("--> onUIRefreshBegin")
    }

    override fun onUIRefreshComplete(frame: PtrFrameLayout) {
        ptr_tv.text = context.getString(R.string.refresh_complete)
        LogUtil.e("--> onUIRefreshComplete")
    }

    override fun onUIReset(frame: PtrFrameLayout) {
        LogUtil.e("--> onUIReset")
    }

    override fun onUIPositionChange(frame: PtrFrameLayout, isUnderTouch: Boolean, status: Byte, ptrIndicator: PtrIndicator) {
        if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {
            if (ptrIndicator.currentPercent > 1) {
                ptr_tv.text = context.getString(R.string.release_to_refresh)
            } else {
                ptr_tv.text = context.getString(R.string.pull_to_refresh)
            }
        }
    }
}