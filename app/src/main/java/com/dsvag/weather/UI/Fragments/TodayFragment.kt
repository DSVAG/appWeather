package com.dsvag.weather.UI.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dsvag.weather.Data.Models.Hourly.HourlyRequest
import com.dsvag.weather.databinding.FragmentTodayBinding
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val preferencesName = "SomeData"

    private lateinit var forecast: HourlyRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)

        sharedPreferences = context!!.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        loadData()


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun loadData() {
        val json = sharedPreferences.getString("Weather", null)
        if (json != null) {
            forecast = Gson().fromJson(json, HourlyRequest::class.java)
        }
    }

    fun utcToDate(dt: Long): LocalDateTime =
        Instant.ofEpochSecond(dt).atZone(ZoneId.systemDefault()).toLocalDateTime()

    companion object {
        const val TAG = "TodayFragment"
    }
}