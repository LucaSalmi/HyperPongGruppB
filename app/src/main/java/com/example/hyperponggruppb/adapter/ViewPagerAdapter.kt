package com.example.hyperponggruppb.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hyperponggruppb.view.fragment.FirstWorldFragment
import com.example.hyperponggruppb.view.fragment.SecondWorldFragment


class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position){
            1 -> FirstWorldFragment()
            0 -> SecondWorldFragment()
            else -> FirstWorldFragment()
        }
    }

}