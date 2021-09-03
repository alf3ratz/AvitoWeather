package hse.ru.avitoweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hse.ru.avitoweather.R
import hse.ru.avitoweather.databinding.DayContainerBinding
import hse.ru.avitoweather.listeners.WeatherListener
import hse.ru.avitoweather.models.HourEntity

class WeatherAdapter(events_: List<HourEntity>, weatherListener_: WeatherListener) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var hourlyWeather: List<HourEntity> = events_
    private var layoutInflater: LayoutInflater? = null
    var eventsListener: WeatherListener = weatherListener_

    inner class WeatherViewHolder(itemLayoutBinding: DayContainerBinding) :
        RecyclerView.ViewHolder(itemLayoutBinding.root) {
        private var containerBinding: DayContainerBinding? = null

        init {
            this.containerBinding = itemLayoutBinding
        }

        fun bindEvent(hourEntity: HourEntity) {
            containerBinding?.weatherInfo = hourEntity
            containerBinding?.executePendingBindings()
            if (containerBinding?.root != null)
                itemView.setOnClickListener {
                    eventsListener.onWeatherClicked(hourEntity)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.context)
        val binding: DayContainerBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.day_container, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindEvent(hourlyWeather[position])
    }

    override fun getItemCount(): Int {
        return hourlyWeather.size
    }
}