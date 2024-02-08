package com.example.erabook.fragments.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.adapters.SearchAdapter
import com.example.erabook.databinding.FragmentSearchBooksBinding
import com.example.erabook.fragments.home.HomeViewModel
import com.example.erabook.fragments.home.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBooksBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var homeViewModel: HomeViewModel

    companion object {
        lateinit var sharedPreferences: SharedPreferences
            private set
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = SearchAdapter()
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
        binding = FragmentSearchBooksBinding.inflate(
            layoutInflater,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            searchList.apply {
                adapter = searchAdapter
            }

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchAdapter.getFilter().filter(newText)
                    return true
                }
            })
            backSearch.setOnClickListener {
                findNavController().navigate(R.id.searchToHome)
            }
            (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavBar).visibility = View.GONE
        }
        homeViewModel.booksList.observe(viewLifecycleOwner) { listOfBooks ->
            searchAdapter.setOriginalList(listOfBooks)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavBar).visibility = View.VISIBLE
    }
}