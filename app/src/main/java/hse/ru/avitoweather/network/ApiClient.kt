package hse.ru.avitoweather.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null
        fun getRetrofit(): Retrofit {
            if (retrofit == null)
                retrofit = Retrofit.Builder()
                    .baseUrl("http://217.23.139.86/")//http://127.0.0.1:5000/ http://localhost:5000/
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit!!
        }
    }//http://217.23.139.86:5000/login_generate/?email=aapetropavlovskiy@edu.hse.ru
}//http://alf3ratz.pythonanywhere.com/