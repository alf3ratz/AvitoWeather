package hse.ru.avitoweather.repositories

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hse.ru.avitoweather.network.ApiClient
import hse.ru.avitoweather.network.ApiService
import hse.ru.avitoweather.responses.HourlyResponse
import hse.ru.avitoweather.responses.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository {
    private var apiService: ApiService = ApiClient.getRetrofit().create(ApiService::class.java)

    fun getCityWeather(city: String, apiKey: String): LiveData<WeatherResponse> {
        val data: MutableLiveData<WeatherResponse> = MutableLiveData()
        apiService.getCityWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(@NonNull call: Call<WeatherResponse>, t: Throwable) {
                data.value = null
                Log.i("fal", "upalo\n${call.request().body()}")
            }

            override fun onResponse(
                @NonNull call: Call<WeatherResponse>,
                @NonNull response: Response<WeatherResponse>
            ) {
                data.value = response.body()
                Log.i("fal", "ne upalo\n${response.isSuccessful}")
            }
        })
        return data
    }

    fun getWeatherAtLastHour(apiKey: String): LiveData<HourlyResponse> {
        val data: MutableLiveData<HourlyResponse> = MutableLiveData()
        apiService.getWeatherAtLastHour(apiKey).enqueue(object : Callback<HourlyResponse> {
            override fun onFailure(@NonNull call: Call<HourlyResponse>, t: Throwable) {
                data.value = null
                Log.i("fal", "upalo\n${call.request().body()}")
            }

            override fun onResponse(
                @NonNull call: Call<HourlyResponse>,
                @NonNull response: Response<HourlyResponse>
            ) {
                data.value = response.body()
                Log.i("fal", "ne upalo\n${response.isSuccessful}")
            }
        })
        return data
    }
}