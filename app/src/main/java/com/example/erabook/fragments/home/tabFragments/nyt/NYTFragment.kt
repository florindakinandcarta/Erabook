package com.example.erabook.fragments.home.tabFragments.nyt

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.erabook.BuildConfig
import com.example.erabook.R
import com.example.erabook.adapters.NYTAdapter
import com.example.erabook.databinding.FragmentNytBinding
import com.example.erabook.fragments.home.HomeViewModel
import com.example.erabook.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NYTFragment : Fragment() {
    private lateinit var binding: FragmentNytBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var nytAdapter: NYTAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNytBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nytAdapter = NYTAdapter()
        binding.parentList.apply {
            adapter = nytAdapter
        }
        homeViewModel.callNYT(BuildConfig.NYT_API_KEY)
        homeViewModel.nyt.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Resource.Error -> {
                    binding.bookAnimation.visibility = View.VISIBLE
                    requireContext().showToast(R.string.error_fetching_data)
                }

                is Resource.Success -> {
                    binding.bookAnimation.visibility = View.GONE
                    nytAdapter.submitList(response.data?.results?.lists)
                }

                else -> {
                    binding.bookAnimation.visibility = View.VISIBLE
                    requireContext().showToast(R.string.default_error)
                }
            }
        }
    }
}