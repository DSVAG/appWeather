package com.dsvag.weather.data.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var fragmentList: MutableList<Fragment> = ArrayList()

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    override fun getItemCount() = fragmentList.size

    fun setData(fragments: List<Fragment>) {
        fragmentList.clear()
        fragmentList.addAll(fragments)
    }
}