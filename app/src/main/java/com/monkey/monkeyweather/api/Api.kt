package com.monkey.monkeyweather.api

import android.content.Context
import com.monkey.monkeyweather.bean.BaseBean
import com.monkey.monkeyweather.bean.ForecastBean
import com.monkey.monkeyweather.bean.NowAirBean
import com.monkey.monkeyweather.bean.WeatherBean
import com.monkey.monkeyweather.util.Constant
import com.monkey.monkeyweather.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


object Api : RetrofitUtil() {

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

    /**
     * 常规天气数据集合
     *
     * 普通用户通过此接口仅获得3天预报数据，认证个人开发者可获取7天预报和24小时内逐3小时预报数据，
     * 实况数据以及免费的生活指数，有权限的付费用户则可获得对应权限的全部数据
     * 本集合接口中，生活指数仅限国内地区，如查询海外城市，则该数据字段将不提供
     */
    fun getWeather(context: Context, listener: OnNetworkRequestListenerAdapter<BaseBean<List<WeatherBean>>>): Disposable {
        return getService().getWeather("北京", Constant.KEY)
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