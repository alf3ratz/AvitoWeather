package hse.ru.avitoweather.responses

import com.google.gson.annotations.SerializedName
import hse.ru.avitoweather.models.HourEntity

class HourlyResponse {
    @SerializedName("timezone")
    var timezone: String = "default"

    @SerializedName("hourly")
    var hourlyWeather: ArrayList<HourEntity> = ArrayList()
}