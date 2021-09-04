package hse.ru.avitoweather.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import hse.ru.avitoweather.adapters.WeatherAdapter
import hse.ru.avitoweather.adapters.WeatherPagerAdapter
import hse.ru.avitoweather.databinding.FragmentMainBinding
import hse.ru.avitoweather.databinding.WeatherFragmentBinding
import hse.ru.avitoweather.listeners.WeatherListener
import hse.ru.avitoweather.models.HourEntity
import hse.ru.avitoweather.responses.HourlyResponse
import hse.ru.avitoweather.responses.WeatherResponse
import hse.ru.avitoweather.ui.activities.MainActivity
import hse.ru.avitoweather.viewmodels.WeatherViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.math.abs


class MainFragment : Fragment(), WeatherListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var binding_: WeatherFragmentBinding
    private lateinit var viewModel: WeatherViewModel
    private var weatherElements: ArrayList<HourEntity> = ArrayList()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherPagerAdapter: WeatherPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding_ = WeatherFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        return binding_.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var city = "Moscow"

        binding.apply {
            buttonForCity.setOnClickListener {
                city = getPickedCity()

                getCityWeather(city)
            }
            buttonForHour.setOnClickListener {
                city = getPickedCity()
                getWeatherAtLastHour(city)
            }

            weatherAdapter = WeatherAdapter(weatherElements, this@MainFragment)
            weatherRecyclerView.adapter = weatherAdapter
            invalidateAll()
        }
        getWeatherAtLastHour(city)
        loadViewPager()

    }

    private fun loadViewPager() {
        weatherPagerAdapter = WeatherPagerAdapter(weatherElements)
        binding_.viewPager.apply {
            offscreenPageLimit = 3
            adapter = weatherPagerAdapter
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(40))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }
            setPageTransformer(compositePageTransformer)
        }
//        eventDetailActivityBinding?.sliderViewPager?.offscreenPageLimit = 1
//        eventDetailActivityBinding?.sliderViewPager?.adapter = ImageSliderAdapter(sliderImages)
//        eventDetailActivityBinding?.sliderViewPager?.visibility = View.VISIBLE
//        eventDetailActivityBinding?.viewFadingEdge?.visibility = View.VISIBLE
//        setupSliderIndicators(sliderImages.size)
//        eventDetailActivityBinding?.sliderViewPager?.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                setCurrentSliderIndicator(position)
//            }
//        })
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
                        weatherElements.addAll(response.hourlyWeather)
                        binding_.weatherInfo = purposeHour
                        weatherAdapter.notifyDataSetChanged()
                        weatherPagerAdapter.notifyDataSetChanged()
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
        if (city.isEmpty()) {
            city = "Moscow"
        }
        return city
    }

    override fun onWeatherClicked(hourEntity: HourEntity) {
        Toast.makeText(context, "ЩИИИИИИИЩ", Toast.LENGTH_LONG).show()
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