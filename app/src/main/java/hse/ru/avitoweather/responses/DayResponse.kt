package hse.ru.avitoweather.responses

import com.google.gson.annotations.SerializedName
import hse.ru.avitoweather.models.City
import hse.ru.avitoweather.models.DayEntity

class DayResponse {
    @SerializedName("lat")
    var lat: Double = 0.0

    @SerializedName("lon")
    var lon: Double = 0.0

    @SerializedName("timezone")
    var city: String = ""

    @SerializedName("timezone_offset")
    var timezoneOffset: String = ""

    @SerializedName("daily")
    var dayWeatherInfo: ArrayList<DayEntity> = ArrayList()


}