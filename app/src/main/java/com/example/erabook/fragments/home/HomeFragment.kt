package com.example.erabook.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.erabook.adapters.PagerAdapter
import com.example.erabook.databinding.FragmentHomeBinding
import com.example.erabook.util.TAB_NAMES
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var demoCollectionPagerAdapter: PagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            layoutInflater,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionPagerAdapter = PagerAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = demoCollectionPagerAdapter
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(TAB_NAMES[position].tabName)
        }.attach()
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }
}
