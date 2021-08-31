package hse.ru.avitoweather.models

import com.google.gson.annotations.SerializedName

class WeatherEntity {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("main")
    var main: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("icon")
    var icon: String = ""

}