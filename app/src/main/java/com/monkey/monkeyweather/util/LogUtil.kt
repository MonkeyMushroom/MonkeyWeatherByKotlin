package com.monkey.monkeyweather.util

import android.util.Log

import com.monkey.monkeyweather.BuildConfig

object LogUtil {

    private val isDebug = BuildConfig.DEBUG
    private const val TAG = "monkey_tag"

    fun v(tag: String, msg: String?) {
        if (isDebug) {
            Log.v(tag, msg ?: "")
        }
    }

    fun i(tag: String, msg: String?) {
        if (isDebug) {
            Log.i(tag, msg ?: "")
        }
    }

    fun d(tag: String, msg: String?) {
        if (isDebug) {
            Log.d(tag, msg ?: "")
        }
    }

    fun w(tag: String, msg: String?) {
        if (isDebug) {
            Log.w(tag, msg ?: "")
        }
    }

    fun e(tag: String, msg: String?) {
        if (isDebug) {
            Log.e(tag, msg ?: "")
        }
    }

    fun v(msg: String?) {
        if (isDebug) {
            Log.v(TAG, msg ?: "")
        }
    }

    fun i(msg: String?) {
        if (isDebug) {
            Log.i(TAG, msg ?: "")
        }
    }

    fun d(msg: String?) {
        if (isDebug) {
            Log.d(TAG, msg ?: "")
        }
    }

    fun w(msg: String?) {
        if (isDebug) {
            Log.w(TAG, msg ?: "")
        }
    }

    fun e(msg: String?) {
        if (isDebug) {
            Log.e(TAG, msg ?: "")
        }
    }
}
