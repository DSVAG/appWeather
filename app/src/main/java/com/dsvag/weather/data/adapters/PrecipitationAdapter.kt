package com.dsvag.weather.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.weather.data.models.request.Minutely
import com.dsvag.weather.data.utils.convertEpochToOffsetDateTime
import com.dsvag.weather.data.utils.timeFormat
import com.dsvag.weather.databinding.RowPrecipitationBinding

class PrecipitationAdapter : RecyclerView.Adapter<PrecipitationAdapter.PrecipitationViewHolder>() {

    private val data: MutableList<Minutely> = ArrayList()
    private var timeZone: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrecipitationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PrecipitationViewHolder(RowPrecipitationBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PrecipitationViewHolder, position: Int) {
        holder.bind(data[position], timeZone)
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: List<Minutely>, timezoneOffset: Int) {
        data.clear()
        data.addAll(newData)

        timeZone = timezoneOffset

        notifyDataSetChanged()
    }

    class PrecipitationViewHolder(
        private val itemBinding: RowPrecipitationBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(minutely: Minutely, timeZone: Int) {
            val date = convertEpochToOffsetDateTime(minutely.dt, (timeZone / 3600))

            itemBinding.precipitation.text = String.format("%.2f", minutely.precipitation)
            itemBinding.time.text = date.format(timeFormat)
        }
    }
}