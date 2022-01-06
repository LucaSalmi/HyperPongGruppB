package com.example.hyperponggruppb

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hyperponggruppb.view.fragment.FirstWorldFragment
import com.example.hyperponggruppb.view.fragment.SecondWorldFragment


class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position){
            0 -> FirstWorldFragment()
            1 -> SecondWorldFragment()
            else -> FirstWorldFragment()
        }
    }

}