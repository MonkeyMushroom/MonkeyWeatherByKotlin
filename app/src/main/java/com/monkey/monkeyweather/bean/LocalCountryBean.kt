package com.monkey.monkeyweather.bean

/**
 * 本地国内外城市bean
 */
data class LocalCountryBean(var Name: String, var Code: String, var State: List<LocalStateBean>) {

    data class LocalStateBean(var Name: String, var Code: String, var City: List<LocalCityBean>) {

        data class LocalCityBean(var Name: String, var Code: String, var Region: List<LocalRegionBean>) {

            data class LocalRegionBean(var Name: String, var Code: String)
        }
    }
}