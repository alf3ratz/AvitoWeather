package hse.ru.avitoweather.models

import com.google.gson.annotations.SerializedName

class DayEntity {
    var cityName: String = ""

    @SerializedName("dt")
    var dateTime: String = ""

    @SerializedName("sunrise")
    var sunrise: String = ""

    @SerializedName("sunset")
    var sunset: String = ""

    @SerializedName("moonset")
    var moonset: String = ""

    @SerializedName("moonrise")
    var moonrise: String = ""

    @SerializedName("moon_phase")
    var moonPhase = ""


    @SerializedName("temp")
    var temperature: Temperature = Temperature()

    @SerializedName("feels_like")
    var feelsLike: FeelsLike = FeelsLike()

    @SerializedName("pressure")
    var pressure: Double = 0.0

    @SerializedName("humidity")
    var humidity: Double = 0.0

    @SerializedName("dew_point")
    var dewPoint: Double = 0.0


    @SerializedName("weather")
    var weather: ArrayList<WeatherEntity> = ArrayList()

    @SerializedName("wind_speed")
    var speed: Double = 0.0

    @SerializedName("wind_deg")
    var windDeg: Double = 0.0

    @SerializedName("wind_gust")
    var windGust: Double = 0.0

    @SerializedName("clouds")
    var clouds: Double = 0.0

    @SerializedName("pop")
    var pop = 0.0

    @SerializedName("uvi")
    var uvi = 0.0

    override fun equals(other: Any?): Boolean {
        //return super.equals(other)
        return dateTime != (other as DayEntity).dateTime
    }

    override fun hashCode(): Int {
        var result = cityName.hashCode()
        result = 31 * result + dateTime.hashCode()
        result = 31 * result + sunrise.hashCode()
        result = 31 * result + sunset.hashCode()
        result = 31 * result + moonset.hashCode()
        result = 31 * result + moonrise.hashCode()
        result = 31 * result + moonPhase.hashCode()
        result = 31 * result + temperature.hashCode()
        result = 31 * result + feelsLike.hashCode()
        result = 31 * result + pressure.hashCode()
        result = 31 * result + humidity.hashCode()
        result = 31 * result + dewPoint.hashCode()
        result = 31 * result + weather.hashCode()
        result = 31 * result + speed.hashCode()
        result = 31 * result + windDeg.hashCode()
        result = 31 * result + windGust.hashCode()
        result = 31 * result + clouds.hashCode()
        result = 31 * result + pop.hashCode()
        result = 31 * result + uvi.hashCode()
        return result
    }
}