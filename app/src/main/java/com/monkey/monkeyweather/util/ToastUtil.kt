package com.monkey.monkeyweather.util

import android.content.Context
import android.widget.Toast

object ToastUtil {

    private var toast: Toast? = null

    val SHOW = 0
    val LONG = 1

    fun show(context: Context?, msg: String) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            } else {
                toast!!.setText(msg)
            }
            toast!!.show()
        }
    }

    fun show(context: Context?, msg: String, time: Int) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, time)
            } else {
                toast!!.setText(msg)
            }
            toast!!.show()
        }
    }
}
