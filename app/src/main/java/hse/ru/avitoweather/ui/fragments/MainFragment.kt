package hse.ru.avitoweather.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import hse.ru.avitoweather.R
import hse.ru.avitoweather.databinding.FragmentMainBinding
import hse.ru.avitoweather.models.HourEntity
import hse.ru.avitoweather.responses.HourlyResponse
import hse.ru.avitoweather.responses.WeatherResponse
import hse.ru.avitoweather.ui.activities.MainActivity
import hse.ru.avitoweather.viewmodels.WeatherViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.ArrayList


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var city = ""

        binding.apply {
            buttonForCity.setOnClickListener {
                city = getPickedCity()

                getCityWeather(city)
            }
            buttonForHour.setOnClickListener {
                city = getPickedCity()
                getWeatherAtLastHour(city)
            }
            weatherRecyclerView.setHasFixedSize(true)
            val weatherAdapter = WeatherAdapter(events, this)
        }

    }

    private fun getCityWeather(city: String) {
        viewModel.getCityWeather(city, "d8c067ca50fc4748821b35656cca8e56")
            .observe(
                (activity as MainActivity),
                { response: WeatherResponse? ->
                    if (response != null) {
                        binding.weatherText.text = response.weather!![0].description
                    } else {
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_LONG)
                            .show()
                    }
                })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getWeatherAtLastHour(city: String) {
        viewModel.getWeatherAtLastHour("d8c067ca50fc4748821b35656cca8e56")
            .observe(
                (activity as MainActivity),
                { response: HourlyResponse? ->
                    if (response != null) {
                        var purposeHour = findLastHourWeather(response.hourlyWeather)
                        binding.weatherTextForHour.text =
                            purposeHour.weather[0].description//response.weather!![0].description
                    } else {
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_LONG)
                            .show()
                    }
                })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun findLastHourWeather(hourlyWeather: ArrayList<HourEntity>): HourEntity {
        val now = LocalDateTime.now()
        var temp = LocalDateTime.MIN
        var purposeHour = HourEntity()

        for (weather in hourlyWeather) {
            val dt = Instant.ofEpochSecond(weather.dateTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            if (dt < now && dt > temp) {
                temp = dt
                purposeHour = weather
            } else {
                return purposeHour
            }
        }
        return purposeHour
    }

    private fun getPickedCity(): String {
        var city = ""
        binding.apply {
            if (moscowBox.isChecked)
                city = "Moscow"
            if (spbBox.isChecked)
                city = "Saint_Petersburg"
            if (kurskBox.isChecked)
                city = "Kursk"
            if (kazanBox.isChecked)
                city = "Kazan"
        }
        return city
    }
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment MainFragment.
//         */
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            MainFragment().apply {
//                arguments = Bundle().apply {
////                    putString(ARG_PARAM1, param1)
////                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}