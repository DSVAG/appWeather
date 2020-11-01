package com.dsvag.weather.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.weather.data.models.request.Hourly
import com.dsvag.weather.data.utils.convertEpochToOffsetDateTime
import com.dsvag.weather.databinding.RowWindBinding

class WindAdapter : RecyclerView.Adapter<WindAdapter.WindViewHolder>() {

    private val data: MutableList<Hourly> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WindViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WindViewHolder(RowWindBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: WindViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setData(newData: List<Hourly>) {
        data.clear()
        data.addAll(newData)

        notifyDataSetChanged()
    }

    class WindViewHolder(private val itemBinding: RowWindBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(hour: Hourly) {
            itemBinding.speed.text = hour.windSpeed.toString()
            itemBinding.degrees.rotation = (hour.windDeg + 180).toFloat() % 360
            itemBinding.time.text =
                convertEpochToOffsetDateTime(hour.dt, 0).hour.toString().plus(":00")
        }
    }
}