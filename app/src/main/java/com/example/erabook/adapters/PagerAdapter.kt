package com.example.erabook.adapters

import ObjectFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.erabook.util.ARG_TAB_NAME
import com.example.erabook.util.TAB_NAMES

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = TAB_NAMES.size
    override fun createFragment(position: Int): Fragment {
        val fragment = ObjectFragment()
        fragment.arguments = Bundle().apply {
            putString(ARG_TAB_NAME, TAB_NAMES[position].type.name)
        }
        return fragment
    }
}