package hse.ru.avitoweather.responses

import com.google.gson.annotations.SerializedName
import hse.ru.avitoweather.models.WeatherEntity

class WeatherResponse {

    @SerializedName("weather")
    var weather: ArrayList<WeatherEntity>? = null

//    @SerializedName("coord")
//    var coord: ArrayList<Double>? = null
//
//    @SerializedName("base")
//    var base: String? = null
//
//    @SerializedName("visibility")
//    var visibility: Int=0


//    @SerializedName("code")
//    var registrationCode:Int = 0
//    @SerializedName("email")
//    var email:String = ""
}