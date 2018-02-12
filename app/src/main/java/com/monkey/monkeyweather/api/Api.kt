package com.monkey.monkeyweather.api

import android.content.Context
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.ForecastBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.bean.NowWeatherBean
import com.monkey.monkeyweather.util.Constant
import com.monkey.monkeyweather.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


object Api : RetrofitUtil() {

    /**
     * 实况天气
     */
    fun getNowWeather(context: Context, listener: OnNetworkRequestListenerAdapter<BaseBean<List<NowWeatherBean>>>): Disposable {
        return getService().getNowWeather("北京", Constant.KEY)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bean ->
                    listener.onSuccess(bean)
                }, { e ->
                    listener.onError(context, e)
                }, { listener.onComplete() })
    }

    /**
     * 空气质量实况
     */
    fun getNowAir(context: Context, listener: OnNetworkRequestListenerAdapter<BaseBean<List<NowAirBean>>>): Disposable {
        return getService().getNowAir("北京", Constant.KEY)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bean ->
                    listener.onSuccess(bean)
                }, { e ->
                    listener.onError(context, e)
                }, { listener.onComplete() })
    }

    /**
     * 3-10天天气预报
     */
    fun getWeatherForecast(context: Context, listener: OnNetworkRequestListenerAdapter<BaseBean<List<ForecastBean>>>): Disposable {
        return getService().getWeatherForecast("北京", Constant.KEY)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bean ->
                    listener.onSuccess(bean)
                }, { e ->
                    listener.onError(context, e)
                }, { listener.onComplete() })
    }

    interface OnNetworkRequestListener<T> {
        fun onSuccess(result: T)
        fun onError(context: Context, e: Throwable)
        fun onComplete()
    }

    open class OnNetworkRequestListenerAdapter<T> : OnNetworkRequestListener<T> {

        override fun onSuccess(result: T) {
            LogUtil.e("--> $result")
        }

        override fun onError(context: Context, e: Throwable) {
            checkException(context, e)
        }

        override fun onComplete() {

        }
    }
}