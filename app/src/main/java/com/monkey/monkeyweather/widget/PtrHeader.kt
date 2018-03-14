package com.monkey.monkeyweather.widget

import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrUIHandler
import `in`.srain.cube.views.ptr.indicator.PtrIndicator
import android.content.Context
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.content.res.AppCompatResources.getDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.util.LogUtil
import kotlinx.android.synthetic.main.header_ptr.view.*


/**
 * 自定义下拉刷新header
 */
class PtrHeader constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr), PtrUIHandler {

    var mLogoVectorDrawable: AnimatedVectorDrawableCompat? = null

    init {
        View.inflate(context, R.layout.header_ptr, this)
    }

    override fun onUIRefreshPrepare(frame: PtrFrameLayout) {
        ptr_tv.text = context.getString(R.string.pull_to_refresh)
        LogUtil.e("--> onUIRefreshPrepare")
    }

    override fun onUIRefreshBegin(frame: PtrFrameLayout) {
        ptr_tv.text = context.getString(R.string.refreshing)
        val imageView = ptr_iv as ImageView
        mLogoVectorDrawable = getDrawable(context, R.drawable.logo_anim) as AnimatedVectorDrawableCompat
        imageView.setImageDrawable(mLogoVectorDrawable)
        mLogoVectorDrawable!!.start()
        LogUtil.e("--> onUIRefreshBegin")
    }

    override fun onUIRefreshComplete(frame: PtrFrameLayout) {
        ptr_tv.text = context.getString(R.string.refresh_complete)
        if (mLogoVectorDrawable != null) {
            mLogoVectorDrawable!!.stop()
        }
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