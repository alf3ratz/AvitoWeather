package hse.ru.avitoweather.network

import hse.ru.avitoweather.responses.HourlyResponse
import hse.ru.avitoweather.responses.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("weather?lang=ru"/*"login_generate?"*/)
    fun getCityWeather(@Query("q") city: String,@Query("appid")appid:String): Call<WeatherResponse>


    @GET("onecall?lat=55.749804&lon=37.621059&units=metric&exclude=current,minutely,daily,alerts&lang=ru")
    fun getWeatherAtLastHour(@Query("appid")appid:String):Call<HourlyResponse>
}