package hse.ru.avitoweather.repositories

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository {
    private var apiService: ApiService = ApiClient.getRetrofit().create(ApiService::class.java)

    fun registerAccount(login: String): LiveData<LoginResponse> {
        val data: MutableLiveData<LoginResponse> = MutableLiveData()
        apiService.getRegistrationCode(login).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(@NonNull call: Call<LoginResponse>, t: Throwable) {
                data.value = null
            }
            override fun onResponse(
                @NonNull call: Call<LoginResponse>,
                @NonNull response: Response<LoginResponse>
            ) {
                data.value = response.body()
            }
        })
        return data
    }
}