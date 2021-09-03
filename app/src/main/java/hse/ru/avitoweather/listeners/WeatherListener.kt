package hse.ru.avitoweather.listeners

import hse.ru.avitoweather.models.HourEntity

interface WeatherListener {
    fun onWeatherClicked(hourEntity: HourEntity)
}