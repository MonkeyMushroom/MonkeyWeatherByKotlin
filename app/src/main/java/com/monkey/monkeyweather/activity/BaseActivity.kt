package com.monkey.monkeyweather.activity

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate


abstract class BaseActivity : AppCompatActivity() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    //6.0以上系统判断权限  --> start

    private val RUNTIME_PERMISSION_REQUEST_CODE = 1
    private var mRuntimePermissionListener: RuntimePermissionListener? = null

    /**
     * 检查运行时权限
     *
     * @param permissions               所检查的权限数组
     * @param runtimePermissionListener 运行时权限监听器
     */
    fun checkRuntimePermission(permissions: Array<String>, runtimePermissionListener: RuntimePermissionListener) {
        mRuntimePermissionListener = runtimePermissionListener
        val deniedPermissionList = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissionList.add(permission)
            }
        }
        if (deniedPermissionList.isEmpty()) {
            mRuntimePermissionListener!!.onRuntimePermissionGranted()
        } else {
            val deniedPermissionArray = deniedPermissionList.toTypedArray()
            ActivityCompat.requestPermissions(this, deniedPermissionArray, RUNTIME_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RUNTIME_PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val deniedPermissionList = ArrayList<String>()
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissionList.add(permissions[i])
                    }
                }
                if (deniedPermissionList.isEmpty()) {
                    mRuntimePermissionListener!!.onRuntimePermissionGranted()
                } else {
                    mRuntimePermissionListener!!.onRuntimePermissionDenied()
                }
            }
            else -> {
            }
        }
    }

    /**
     * 运行时权限监听器
     */
    interface RuntimePermissionListener {
        /**
         * 允许所请求的全部权限
         */
        fun onRuntimePermissionGranted()

        /**
         * 拒绝所请求的部分或全部权限
         */
        fun onRuntimePermissionDenied()
    }
//6.0以上系统判断权限  --> end
}
