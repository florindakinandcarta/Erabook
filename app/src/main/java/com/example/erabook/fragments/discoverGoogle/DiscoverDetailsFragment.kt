package com.example.erabook.fragments.discoverGoogle

import Resource
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.erabook.R
import com.example.erabook.databinding.FragmentBookDetailsBinding
import com.example.erabook.util.loadImageFromUrl
import com.example.erabook.util.openLinkBrowser
import com.example.erabook.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private val googleBooksViewModel: GoogleBooksViewModel by viewModels()
    private val args: DiscoverFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backBookDetails.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            val searchTerm = args.bookName?.replace(" ", "+")
            googleBooksViewModel.fetchBooks(searchTerm, 0)
            googleBooksViewModel.response_books.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Error -> {
                        requireContext().showToast(R.string.error_fetching_data)
                    }

                    is Resource.Success -> {
                        googleBooksViewModel.loading_books.observe(viewLifecycleOwner) { loader ->
                            if (loader) {
                                progressBar.visibility = View.VISIBLE
                            } else {
                                progressBar.visibility = View.GONE
                            }
                        }
                        val volumeInfoList = response.data?.items?.find {
                            it.volumeInfo?.title == args.bookName
                        }
                        bookNameDetails.text = volumeInfoList?.volumeInfo?.title
                        bookAuthorDetails.text =
                            volumeInfoList?.volumeInfo?.authors?.get(0) ?: "Authors not found"
                        releaseDateNumber.text = volumeInfoList?.volumeInfo?.publishedDate
                        numberOfPages.text = volumeInfoList?.volumeInfo?.pageCount.toString()
                        paragraphText.text = volumeInfoList?.volumeInfo?.description
                        bookImageDetails.loadImageFromUrl(volumeInfoList?.volumeInfo?.imageLinks?.thumbnail)
                        buyBook.setOnClickListener {
                            openLinkBrowser(volumeInfoList?.volumeInfo?.title.toString(),requireContext())
                        }
                        shareBook.setOnClickListener {
                            val bookDetailsIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT, getString(
                                        R.string.share_book,
                                        volumeInfoList?.volumeInfo?.title,
                                        volumeInfoList?.volumeInfo?.authors?.get(0)
                                            ?: "Authors not found",
                                        volumeInfoList?.volumeInfo?.pageCount.toString(),
                                        volumeInfoList?.volumeInfo?.publishedDate
                                    )
                                )
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    getString(R.string.share_book_subject)
                                )
                            }
                            val chooserIntent = Intent.createChooser(
                                bookDetailsIntent,
                                getString(R.string.send_details)
                            )
                            startActivity(chooserIntent)
                        }
                    }

                    else -> {
                        requireContext().showToast(R.string.default_error)
                    }
                }

            }
        }
    }
}