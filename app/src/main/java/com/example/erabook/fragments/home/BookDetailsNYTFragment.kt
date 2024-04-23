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
import com.example.erabook.data.models.nyt.Books
import com.example.erabook.databinding.FragmentNytBookDetailsBinding
import com.example.erabook.util.CachingNYT
import com.example.erabook.util.loadImage
import com.example.erabook.util.openLinkBrowser
import com.example.erabook.util.showToast
import com.rommansabbir.networkx.extension.isInternetConnectedFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookDetailsNYTFragment : Fragment() {
  private lateinit var binding: FragmentNytBookDetailsBinding
  private val args: BookDetailsNYTFragmentArgs by navArgs()
  private val homeViewModel: HomeViewModel by viewModels()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    binding = FragmentNytBookDetailsBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.apply {
      backBookDetails.setOnClickListener {
        requireActivity().onBackPressedDispatcher.onBackPressed()
      }
      loadData()
    }
  }

  private fun loadData() {
    lifecycleScope.launch {
      isInternetConnectedFlow.collectLatest {
        if (it) {
          homeViewModel.callNYT(BuildConfig.NYT_API_KEY, requireContext())
          homeViewModel.nyt.observe(viewLifecycleOwner) { response ->
            when (response) {
              is Resource.Error -> {
                binding.loader.visibility = View.GONE
                requireContext().showToast(R.string.error_fetching_data)
              }
              is Resource.Success -> {
                binding.loader.visibility = View.GONE
                val singleBook =
                    response.data
                        ?.results
                        ?.lists
                        ?.flatMap { list -> list.books!! }
                        ?.find { book -> book.title == args.nytBookTitle }
                bind(singleBook)
              }
              else -> {
                requireContext().showToast(R.string.default_error)
              }
            }
          }
        } else {
          val offlineData = CachingNYT.getCachedNYTBooks(requireContext())
          val singleBook =
              offlineData
                  ?.results
                  ?.lists
                  ?.flatMap { list -> list.books!! }
                  ?.find { book -> book.title == args.nytBookTitle }
          bind(singleBook)
          binding.loader.visibility = View.GONE
        }
      }
    }
  }

  private fun bind(book: Books?) {
    binding.apply {
      book?.let {
        bookImageDetails.loadImage(book.bookImage)
        bookNameDetails.text = book.title
        bookAuthorDetails.text = book.author
        weeksOnTheListNumber.text = book.weeksOnList
        rankNumber.text = book.rank
        description.text = book.description
        buyBook.setOnClickListener { openLinkBrowser(book.title.toString(), requireContext()) }
      }
    }
  }
}
