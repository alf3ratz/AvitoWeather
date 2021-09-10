package hse.ru.avitoweather.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hse.ru.avitoweather.R
import hse.ru.avitoweather.databinding.PagerContainerBinding
import hse.ru.avitoweather.models.DayEntity
import hse.ru.avitoweather.models.HourEntity
import java.lang.NumberFormatException
import java.time.Instant
import java.time.ZoneId

class WeatherPagerAdapter(sliderWeatherInfo: ArrayList<DayEntity>) :
    RecyclerView.Adapter<WeatherPagerAdapter.WeatherPagerViewHolder>() {
    private var weatherInfo: ArrayList<DayEntity> = ArrayList()
    private var layoutInflater: LayoutInflater? = null

    init {
        this.weatherInfo = sliderWeatherInfo
    }

    inner class WeatherPagerViewHolder(itemContainerSliderImageBinding: PagerContainerBinding) :
        RecyclerView.ViewHolder(itemContainerSliderImageBinding.root) {
        private var binding: PagerContainerBinding? = null

        init {
            this.binding = itemContainerSliderImageBinding
        }

        fun bindSliderImage(weatherInfo: DayEntity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    weatherInfo.dateTime = Instant.ofEpochSecond(weatherInfo.dateTime.toLong())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime().toString().substringBefore("T")
                    weatherInfo.sunrise = Instant.ofEpochSecond(weatherInfo.sunrise.toLong())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime().toString().substringAfter("T")
                    weatherInfo.sunset = Instant.ofEpochSecond(weatherInfo.sunset.toLong())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime().toString().substringAfter("T")
                } catch (e: NumberFormatException) {
                    Log.i("норм", "первый элемент ${weatherInfo.dateTime}")
                }
            }
            binding?.weatherInfo = weatherInfo
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherPagerViewHolder {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.context)
        val sliderImageBinding: PagerContainerBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.pager_container, parent, false
        )
        return WeatherPagerViewHolder(sliderImageBinding)
    }

    override fun onBindViewHolder(holder: WeatherPagerViewHolder, position: Int) {
        weatherInfo?.get(position)
            ?.let { holder.bindSliderImage(it) }

    }

    override fun getItemCount(): Int {
        return weatherInfo!!.size
    }
}