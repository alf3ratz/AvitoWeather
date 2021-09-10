package hse.ru.avitoweather.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import hse.ru.avitoweather.R
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import hse.ru.avitoweather.adapters.WeatherAdapter
import hse.ru.avitoweather.adapters.WeatherPagerAdapter
import hse.ru.avitoweather.databinding.WeatherFragmentBinding
import hse.ru.avitoweather.models.DayEntity
import hse.ru.avitoweather.models.HourEntity
import hse.ru.avitoweather.responses.DayResponse
import hse.ru.avitoweather.responses.HourlyResponse
import hse.ru.avitoweather.responses.WeatherResponse
import hse.ru.avitoweather.ui.activities.MainActivity
import hse.ru.avitoweather.viewmodels.WeatherViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.abs
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices


class MainFragment : Fragment() {

    private var binding: WeatherFragmentBinding? = null
    private lateinit var viewModel: WeatherViewModel
    private var hourlyWeather: ArrayList<HourEntity> = ArrayList()
    private var dailyWeather: ArrayList<DayEntity> = ArrayList()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherPagerAdapter: WeatherPagerAdapter

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var settingsClient: SettingsClient // Доступ к настройкам
    private lateinit var locationRequest: LocationRequest // Сохранение данных запроса
    private lateinit var locationSettingsRequest: LocationSettingsRequest // Определние настроек девайса пользователя
    private lateinit var locationCallback: LocationCallback // События определения местоположения
    private var location: Location? = null// Широта и долгота пользователя

    private val cities = arrayOf(
        "Moscow",
        "Kursk",
        "Kazan",
        "Saint-Petersburg",
        "Nizhniy Novgorod",
        "Ufa",
        "Ekaterinburg",
        "По местоположению"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        if (checkLocationPermission() && fusedLocationProviderClient != null) startLocationUpdates()
        //else locationPermission()
    }

    /**
     * Проверка разрешений для получения местоположения
     */
    private fun checkLocationPermission(): Boolean {
        val state =
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        return state == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Вспомогательныйметод для получения местоположения
     */
    private fun makeLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    /**
     * Вспомогательныйметод для получения местоположения
     */
    private fun makeLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                location = p0.lastLocation
            }
        }
    }

    /**
     * Вспомогательныйметод для получения местоположения
     */
    private fun makeLocationSettings() {
        val settingsBuilder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        settingsBuilder.addLocationRequest(locationRequest)
        locationSettingsRequest = settingsBuilder.build()
    }

    /**
     * Прекращение получения местоположения
     */
    private fun stopLocationUpdates() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    /**
     * Вспомогательныйметод для получения местоположения
     */
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), Array(1) { Manifest.permission.ACCESS_FINE_LOCATION },
                SETTINGS_CODE
            )

        } else if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), Array(1) { Manifest.permission.ACCESS_COARSE_LOCATION },
                SETTINGS_CODE
            )
        } else {
            setLocation()
        }

    }

    /**
     * Вспомогательныйметод для получения местоположения
     */
    @SuppressLint("MissingPermission")
    private fun setLocation() {
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )?.addOnFailureListener {
            when ((it as ApiException).statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val ex: ResolvableApiException = it as ResolvableApiException
                        ex.startResolutionForResult(requireActivity(), SETTINGS_CODE)
                    } catch (e: IntentSender.SendIntentException) {
                        Toast.makeText(
                            context,
                            "Проблемы с подтверждением доступа к геолокации",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    Toast.makeText(
                        context,
                        "Подтвердите настройки геолокации",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WeatherFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadViewPager()
        binding!!.apply {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View, position: Int, selectedId: Long
                ) {
                    makeChoise(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    /**
     * Метод, работающий с аргументами выпадающего списка
     */
    private fun makeChoise(position: Int) {
        if (dailyWeather.isNotEmpty()) {
            dailyWeather.clear()
        }
        when (cities[position]) {
            "Moscow" -> getWeatherAtLastDay("55.749804", "37.621059", position)
            "Kazan" -> getWeatherAtLastDay("55.796127", "49.106414", position)
            "Kursk" -> getWeatherAtLastDay("51.730846", "36.193015", position)
            "Saint-Petersburg" -> getWeatherAtLastDay(
                "59.939099",
                "30.315877",
                position
            )
            "Nizhniy Novgorod" -> getWeatherAtLastDay(
                "56.326769",
                "44.006565",
                position
            )
            "Ufa" -> getWeatherAtLastDay("54.735098", "55.958338", position)
            "Ekaterinburg" -> getWeatherAtLastDay("56.838011", "60.597474", position)
            "По местоположению" -> {
                weatherPagerAdapter.notifyDataSetChanged()
                getWeatherByGeo(position)
            }
        }
        Toast.makeText(
            context,
            "Ваш выбор: " + cities[position], Toast.LENGTH_SHORT
        ).show()
    }


    @SuppressLint("MissingPermission")
    private fun getWeatherByGeo(position: Int) {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        settingsClient = LocationServices.getSettingsClient(requireActivity())
        makeLocationRequest()
        makeLocationCallback()
        makeLocationSettings()
        startLocationUpdates()
        val locationResult = fusedLocationProviderClient?.lastLocation
        locationResult?.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                location = task.result
                if (location != null) {
                    getWeatherAtLastDay(
                        location?.latitude.toString(),
                        location?.longitude.toString(), position
                    )
                } else {
                    Log.i("failed","fail2")
                }
            } else {
                weatherPagerAdapter.notifyDataSetChanged()
                Log.i("failed","fail1")
            }
        }

    }

    /**
     * Создание адаптера для погоды на неделю.
     */
    private fun loadViewPager() {
        weatherPagerAdapter = WeatherPagerAdapter(dailyWeather)
        binding!!.viewPager.apply {
            offscreenPageLimit = 3
            adapter = weatherPagerAdapter
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(40))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = /*0.85f*/0.85f + r * 0.15f
                // page.
            }
            setPageTransformer(compositePageTransformer)
        }
    }

    private fun getCityWeather(city: String) {
        viewModel.getCityWeather(city, "d8c067ca50fc4748821b35656cca8e56")
            .observe(
                (activity as MainActivity),
                { response: WeatherResponse? ->
                    if (response != null) {
                        //binding.weatherText.text = response.weather!![0].description
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
                        val purposeHour = findLastHourWeather(response.hourlyWeather)
                        //binding.weatherTextForHour.text =
                        purposeHour.weather[0].description//response.weather!![0].description
                        //weatherElements.addAll(response.hourlyWeather)
                        //binding_.weatherInfo = purposeHour
                        weatherAdapter.notifyDataSetChanged()
                        weatherPagerAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_LONG)
                            .show()
                    }
                })
    }

    /**
     * Получение погоды за сегодняшний день и за неделю.
     */
    private fun getWeatherAtLastDay(lat: String, lon: String, position: Int) {
        viewModel.getWeatherAtLastDay(lat, lon, "d8c067ca50fc4748821b35656cca8e56")
            .observe((activity as MainActivity)) { response: DayResponse? ->
                if (response != null) {
                    val dayEntity = response.dayWeatherInfo[0]
                    dailyWeather.addAll(
                        response.dayWeatherInfo.subList(
                            1,
                            response.dayWeatherInfo.size
                        )
                    )
                    Toast.makeText(
                        context,
                        "${dailyWeather.size}",
                        Toast.LENGTH_LONG
                    ).show()
                    weatherPagerAdapter.notifyDataSetChanged()
                    val cities = resources.getStringArray(R.array.cityNames)
                    dayEntity.apply {
                        cityName = "<" + cities[position] + ">"
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            dateTime = Instant.ofEpochSecond(dayEntity.dateTime.toLong())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime().toString().substringBefore("T")
                        }
                    }
                    binding!!.weatherInfo = dayEntity
                } else {
                    Toast.makeText(
                        context,
                        "При получении данных произошла ошибка",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
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


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val SETTINGS_CODE: Int = 123
    }

}