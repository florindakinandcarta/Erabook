package com.example.erabook.fragments.discoverGoogle

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.adapters.DiscoverAdapter
import com.example.erabook.databinding.FragmentDiscoverBinding
import com.example.erabook.util.showToast
import com.rommansabbir.networkx.extension.isInternetConnectedFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscoverFragment : Fragment() {
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var discoverAdapter: DiscoverAdapter
    private val sharedViewModel: SharedGoogleBooksViewModel by viewModels({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discoverAdapter = DiscoverAdapter()

        binding.apply {
            searchListGoogle.apply {
                adapter = discoverAdapter
            }
            searchSomething.visibility = View.VISIBLE
            loader.visibility = View.GONE
            qrCodeButton.setOnClickListener {
                findNavController().navigate(R.id.discoverToCamera)
            }
        }
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            isInternetConnectedFlow.collectLatest {
                when (it) {
                    true -> {
                        binding.apply {
                            searchBarGoogle.setOnQueryTextListener(object :
                                SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    val searchTerm = query?.replace(" ", "+")
                                    sharedViewModel.fetchBooks(searchTerm, 0)
                                    loadMore.setOnClickListener {
                                        sharedViewModel.fetchBooks(searchTerm, 20)
                                    }
                                    loader.visibility = View.VISIBLE
                                    searchSomething.visibility = View.GONE
                                    searchBarGoogle.clearFocus()
                                    return true
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    return false
                                }
                            })
                            sharedViewModel.isResponseZero.observe(viewLifecycleOwner) { isResponseZero ->
                                if (isResponseZero) {
                                    discoverAdapter.submitList(emptyList())
                                    binding.apply {
                                        loadMore.visibility = View.GONE
                                        searchSomething.visibility = View.VISIBLE
                                        searchBarGoogle.setQuery("", false)
                                    }
                                }
                            }
                            sharedViewModel.response_books.observe(viewLifecycleOwner) { response ->
                                when (response) {
                                    is Resource.Error -> {
                                        requireContext().showToast(R.string.error_fetching_data)
                                    }

                                    is Resource.Success -> {
                                        val volumeInfoList =
                                            response.data?.items?.map { it.volumeInfo }
                                        volumeInfoList?.let { discoverAdapter.submitList(it) }
                                        loader.visibility = View.GONE
                                        loadMore.visibility = View.VISIBLE
                                        searchSomething.visibility = View.GONE
                                        searchBarGoogle.clearFocus()
                                    }

                                    else -> {
                                        requireContext().showToast(R.string.default_error)
                                    }
                                }

                            }
                        }
                    }
                    else -> {
                        binding.checkConnection.visibility = View.VISIBLE
                        binding.searchSomething.visibility = View.GONE
                    }
                }
            }
        }
    }
}