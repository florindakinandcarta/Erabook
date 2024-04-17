package com.example.erabook.fragments.home

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.erabook.BuildConfig
import com.example.erabook.R
import com.example.erabook.adapters.NYTChildGenreAdapter
import com.example.erabook.databinding.FragmentNytGenreBinding
import com.example.erabook.util.showToast
import com.rommansabbir.networkx.extension.isInternetConnectedFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NYTGenreFragment : Fragment() {
    private lateinit var binding: FragmentNytGenreBinding
    private lateinit var nytGenreAdapter: NYTChildGenreAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private val args: NYTGenreFragmentArgs by navArgs()

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
        nytGenreAdapter = NYTChildGenreAdapter()
        binding.genreList.apply {
            adapter = nytGenreAdapter
        }
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.genreTitle.text = args.listName
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            isInternetConnectedFlow.collectLatest {
                when (it) {
                    true -> {
                        binding.genreTitle.text = args.listName
                        homeViewModel.callNYT(BuildConfig.NYT_API_KEY)
                        homeViewModel.nyt.observe(viewLifecycleOwner) { response ->
                            when (response) {
                                is Resource.Error -> {
                                    binding.loader.visibility = View.VISIBLE
                                    requireContext().showToast(R.string.error_fetching_data)
                                }

                                is Resource.Success -> {
                                    binding.loader.visibility = View.GONE
                                    val filteredList =
                                        response.data?.results?.lists?.filter { list ->
                                            list.listName == args.listName
                                        }
                                    filteredList?.forEach { list ->
                                        nytGenreAdapter.submitList(list.books)
                                    }
                                }

                                else -> {
                                    binding.loader.visibility = View.VISIBLE
                                    requireContext().showToast(R.string.default_error)
                                }
                            }
                        }
                    }

                    else -> {
                        binding.loader.visibility = View.GONE
                        requireContext().showToast(R.string.check_network_connection)
                    }
                }
            }
        }
    }
}