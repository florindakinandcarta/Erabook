package com.example.erabook.fragments.home.tabFragments.nyt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.erabook.R
import com.example.erabook.adapters.NYTChildAdapter
import com.example.erabook.databinding.FragmentNytGenreBinding
import com.example.erabook.fragments.home.HomeViewModel
import com.example.erabook.util.showToast

class NYTGenre : Fragment() {
    private lateinit var binding: FragmentNytGenreBinding
    private lateinit var nytGenreAdapter: NYTChildAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNytGenreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nytGenreAdapter = NYTChildAdapter()
        binding.genreList.apply {
            adapter = nytGenreAdapter
        }
        homeViewModel.nyt.observe(viewLifecycleOwner){response ->
            when (response) {
                is Resource.Error -> {
                    requireContext().showToast(R.string.error_fetching_data)
                }
                is Resource.Success -> {
                    response.data?.results?.lists?.forEach {list ->
                        if(list.listName == "Combined Print and E-Book Fiction"){
                            nytGenreAdapter.submitList(list.books)
                        }
                    }
                }
                else -> {
                    requireContext().showToast(R.string.default_error)
                }
            }
        }
    }
}