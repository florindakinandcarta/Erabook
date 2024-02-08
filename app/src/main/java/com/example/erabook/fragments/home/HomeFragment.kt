package com.example.erabook.fragments.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.adapters.HomeAdapter
import com.example.erabook.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeViewModel: HomeViewModel

    companion object {
        lateinit var sharedPreferences: SharedPreferences
            private set
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeAdapter = HomeAdapter { clickedBook ->
            homeViewModel.addToFavorites(clickedBook)
        }

        homeViewModel = ViewModelProvider(
            this, HomeViewModelFactory(requireContext().assets.open("books.json")
                .bufferedReader().use { it.readText() })
        )[HomeViewModel::class.java]

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

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
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            homeListRecycleView.apply {
                adapter = homeAdapter
            }
        }
        homeViewModel.booksList.observe(viewLifecycleOwner) { listOfBooks ->
            homeAdapter.submitList(listOfBooks)
        }

        binding.topAppBar.inflateMenu(R.menu.top_app_bar_menu)
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.searchFragmentIcon -> {
                    findNavController().navigate(R.id.homeToSearch)
                    true
                }

                else -> false
            }
        }
    }

}