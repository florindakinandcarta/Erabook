package com.example.erabook.fragments.home

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.erabook.BuildConfig
import com.example.erabook.R
import com.example.erabook.adapters.NYTAdapter
import com.example.erabook.databinding.FragmentHomeBinding
import com.example.erabook.util.showToast
import com.rommansabbir.networkx.extension.isInternetConnectedFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var nytAdapter: NYTAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nytAdapter = NYTAdapter()
        binding.parentList.apply {
            adapter = nytAdapter
        }
        loadData()
    }
    private fun loadData(){
        lifecycleScope.launch {
            isInternetConnectedFlow
                .debounce(500L)
                .collectLatest {
                    if (it) {
                        homeViewModel.callNYT(BuildConfig.NYT_API_KEY)
                        homeViewModel.nyt.observe(viewLifecycleOwner) { response ->
                            when (response) {
                                is Resource.Error -> {
                                    binding.bookAnimation.visibility = View.GONE
                                    requireContext().showToast(R.string.error_fetching_data)
                                }
                                is Resource.Success -> {
                                    binding.bookAnimation.visibility = View.GONE
                                    nytAdapter.submitList(response.data?.results?.lists)
                                }
                                else -> {
                                    requireContext().showToast(R.string.default_error)
                                }

                            }
                        }
                    } else {
                        binding.bookAnimation.visibility = View.GONE
                        requireContext().showToast(R.string.check_network_connection)
                    }
                }
        }
    }
}