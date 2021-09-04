package hse.ru.avitoweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hse.ru.avitoweather.R
import hse.ru.avitoweather.databinding.PagerContainerBinding
import hse.ru.avitoweather.models.HourEntity

class WeatherPagerAdapter(sliderWeatherInfo: ArrayList<HourEntity>) :
    RecyclerView.Adapter<WeatherPagerAdapter.WeatherPagerViewHolder>() {
    private var weatherInfo: ArrayList<HourEntity>? = null
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

        fun bindSliderImage(weatherInfo:HourEntity) {
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