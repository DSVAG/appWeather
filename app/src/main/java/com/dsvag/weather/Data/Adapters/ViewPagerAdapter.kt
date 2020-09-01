package com.dsvag.weather.Data.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dsvag.weather.UI.Fragments.ForecastFragment
import com.dsvag.weather.UI.Fragments.TodayFragment
import com.dsvag.weather.UI.Fragments.TomorrowFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> TomorrowFragment()
            else -> ForecastFragment()
        }
    }
}