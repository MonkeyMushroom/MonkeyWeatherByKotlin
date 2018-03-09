package com.monkey.monkeyweather.bean

/**
 * 本地国内外城市bean
 */
data class LocalCountryBean(var name: String, var code: String, var province: List<LocalProvinceBean>) {

    data class LocalProvinceBean(var name: String, var code: String, var city: List<LocalCityBean>) {

        data class LocalCityBean(var name: String, var Code: String, var region: List<LocalRegionBean>) {

            data class LocalRegionBean(var name: String, var code: String)
        }
    }
}