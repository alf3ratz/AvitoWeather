package hse.ru.avitoweather.viewmodels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WeatherViewModel(@NonNull application: Application) : AndroidViewModel(application) {
    private var loginRepository: WeatherRepository =
        WeatherRepository()

    fun registerAccount(login:String): LiveData<LoginResponse> {
        return loginRepository.registerAccount(login)
    }

}