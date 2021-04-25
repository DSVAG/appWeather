package com.dsvag.weather.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dsvag.weather.data.models.Units
import com.dsvag.weather.data.models.request.Daily
import com.dsvag.weather.data.utils.convertEpochToOffsetDateTime
import com.dsvag.weather.data.utils.dateFormat
import com.dsvag.weather.data.utils.degreesToDirection
import com.dsvag.weather.data.utils.timeFormat
import com.dsvag.weather.databinding.RowDailyBinding

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    private val dailyList: MutableList<Daily> = mutableList()
    private var timeZone: Int = 0
    private lateinit var units: Units

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DailyViewHolder(
            RowDailyBinding.inflate(inflater, parent, false), timeZone, units
        )
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(dailyList[position])
        holder.itemView.setOnClickListener { expand(position) }
    }

    override fun getItemCount(): Int = dailyList.size

    fun setData(daily: List<Daily>, timezoneOffset: Int, units: Units) {
        dailyList.clear()
        dailyList.addAll(daily)

        timeZone = timezoneOffset

        this.units = units

        notifyDataSetChanged()
    }

    private fun expand(position: Int) {
        dailyList[position].isExpand = !dailyList[position].isExpand
        notifyItemChanged(position)
    }

    class DailyViewHolder(
        private val itemBinding: RowDailyBinding,
        private val timeZone: Int,
        private val units: Units,
    ) : RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(daily: Daily) {
            itemBinding.expand.visibility = if (daily.isExpand) View.VISIBLE else View.GONE

            val url = "https://openweathermap.org/img/wn/${daily.weather[0].icon}@2x.png"
            itemBinding.date.text =
                convertEpochToOffsetDateTime(daily.dt, timeZone).format(dateFormat)

            itemBinding.condition.text = daily.weather[0].description
            Glide.with(itemBinding.root).load(url).centerCrop().into(itemBinding.icon)

            itemBinding.maxTemp.text = String.format("%.0f", daily.temp.max).plus(units.degree)
            itemBinding.minTemp.text = String.format("%.0f", daily.temp.min).plus(units.degree)

            itemBinding.wind.text = daily.windSpeed.toString()
                .plus("${units.windSpeed} ${degreesToDirection(daily.windDeg)}")
            itemBinding.humidity.text = daily.humidity.toString().plus("%")
            itemBinding.uvIndex.text = String.format("%.0f", daily.uvi)
            itemBinding.rainChance.text = String.format("%.0f", daily.pop * 100).plus("%")

            val sunRise = convertEpochToOffsetDateTime(daily.sunrise, timeZone).format(timeFormat)
            val sunSet = convertEpochToOffsetDateTime(daily.sunset, timeZone).format(timeFormat)
            itemBinding.sun.text = sunRise.plus(", $sunSet")
        }
    }
}