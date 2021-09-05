package hse.ru.avitoweather.models

import com.google.gson.annotations.SerializedName

class Temperature {
    @SerializedName("day")
    var dayTemp: Double = 0.0

    @SerializedName("morn")
    var morningTemp: Double = 0.0

    @SerializedName("eve")
    var eveningTemp: Double = 0.0

    @SerializedName("night")
    var nightTemp: Double = 0.0

    @SerializedName("max")
    var maxTemperature: Double = 0.0

    @SerializedName("min")
    var minTemperature:Double = 0.0
}