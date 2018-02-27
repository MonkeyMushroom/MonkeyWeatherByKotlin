package com.monkey.monkeyweather.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * 屏幕相关
 */
class ScreenUtil {

    companion object {

        /**
         * 获得屏幕宽度
         *
         * @param context
         * @return
         */
        fun getScreenWidth(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.widthPixels
        }

        /**
         * 获得屏幕高度
         */
        fun getScreenHeight(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(outMetrics)
            return outMetrics.heightPixels
        }

        /**
         * 获取状态栏高度
         *
         * @param context context
         * @return 状态栏高度
         */
        fun getStatusBarHeight(context: Context): Int {
            // 获得状态栏高度
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        }
    }
}