package hse.ru.avitoweather.viewmodels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hse.ru.avitoweather.repositories.WeatherRepository
import hse.ru.avitoweather.responses.HourlyResponse
import hse.ru.avitoweather.responses.WeatherResponse

class WeatherViewModel(@NonNull application: Application) : AndroidViewModel(application) {
    private var loginRepository: WeatherRepository =
        WeatherRepository()

    fun getCityWeather(city:String,apiKey:String): LiveData<WeatherResponse> {
        return loginRepository.getCityWeather(city,apiKey)
    }


    fun getWeatherAtLastHour(apiKey:String): LiveData<HourlyResponse> {
        return loginRepository.getWeatherAtLastHour(apiKey)
    }
}