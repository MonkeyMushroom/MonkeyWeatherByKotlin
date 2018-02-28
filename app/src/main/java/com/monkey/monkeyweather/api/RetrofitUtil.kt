package com.monkey.monkeyweather.api

import android.content.Context
import com.monkey.monkeyweather.BuildConfig
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.util.Constant
import com.monkey.monkeyweather.util.LogUtil
import com.monkey.monkeyweather.util.ToastUtil
import com.monkey.monkeyweather.util.WeatherSignUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit


/**
 * Retrofit工具类
 */
open class RetrofitUtil {

    private var apiService: ApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            if (BuildConfig.DEBUG) {
                LogUtil.e("http message " + message)
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    /**
     * 获取Retrofit实例
     */
    fun getService(): ApiService {
        return apiService
    }

    fun checkException(context: Context, e: Throwable): String {
        val msg: String
        when (e) {
            is UnknownHostException -> msg = context.getString(R.string.no_internet)
            is NoDataException -> msg = context.getString(R.string.no_data)
            is SocketTimeoutException -> msg = context.getString(R.string.socket_timeout)
            else -> {
                msg = context.getString(R.string.unknown_exception)
                e.printStackTrace()
            }
        }
        ToastUtil.show(context, msg)
        return msg
    }

    private class NoDataException : Throwable()

    fun getBaseParams(location: String): HashMap<String, String> {
        val username = Constant.USERNAME
        val t = System.currentTimeMillis()
        val signMap = HashMap<String, String>()
        signMap["location"] = location
        signMap["username"] = username
        signMap["t"] = t.toString()
        val sign = WeatherSignUtil.getSignature(signMap, Constant.KEY)
        val paramsMap = HashMap<String, String>()
        paramsMap["username"] = username
        paramsMap["t"] = t.toString()
        paramsMap["sign"] = sign
        return paramsMap
    }


}