package com.dsvag.weather.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsvag.weather.data.models.request.Hourly
import com.dsvag.weather.data.utils.convertEpochToOffsetDateTime
import com.dsvag.weather.data.utils.timeFormat
import com.dsvag.weather.databinding.RowTemperatureBinding

class TemperatureAdapter : RecyclerView.Adapter<TemperatureAdapter.TemperatureViewHolder>() {

    private val data: MutableList<Hourly> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemperatureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TemperatureViewHolder(RowTemperatureBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TemperatureViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: List<Hourly>) {
        data.clear()
        data.addAll(newData)

        notifyDataSetChanged()
    }

    class TemperatureViewHolder(
        private val itemBinding: RowTemperatureBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(hour: Hourly) {
            val url = "https://openweathermap.org/img/wn/${hour.weather[0].icon}@2x.png"

            itemBinding.temp.text = String.format("%.0f°", hour.temp)
            itemBinding.time.text =
                convertEpochToOffsetDateTime(hour.dt, 0).format(timeFormat)
            Glide.with(itemBinding.root).load(url).centerInside().into(itemBinding.icon)
        }
    }
}