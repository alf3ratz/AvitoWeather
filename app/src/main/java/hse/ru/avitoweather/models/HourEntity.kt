package hse.ru.avitoweather.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class HourEntity {
    var dateTimeString: String = ""

    @SerializedName("dt")
    var dateTime: Long = 0

    @SerializedName("temp")
    var temperature: Double = 0.0

    @SerializedName("feels_like")
    var feelsLike: Double = 0.0

    @SerializedName("humidity")
    var humidity: Double = 0.0

    @SerializedName("clouds")
    var cloudness: Double = 0.0

    @SerializedName("wind_speed")
    var windSpeed: Double = 0.0

    @SerializedName("wind_deg")
    var windDirection: Int = 0

    @SerializedName("pop")
    var probabilityOfPrecipitation: Double = 0.0

    @SerializedName("weather")
    var weather: ArrayList<WeatherEntity> = ArrayList()


}