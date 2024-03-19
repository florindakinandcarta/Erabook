package com.example.erabook.fragments.discoverGoogle

import Resource
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.erabook.R
import com.example.erabook.data.models.Items
import com.example.erabook.databinding.FragmentBookDetailsBinding
import com.example.erabook.util.loadImageFromUrl
import com.example.erabook.util.openLinkBrowser
import com.example.erabook.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private val args: DiscoverFragmentArgs by navArgs()
    private val sharedViewModel: SharedGoogleBooksViewModel by viewModels({ requireActivity() })
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
            sharedViewModel.response_books.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Error -> {
                        requireContext().showToast(R.string.error_fetching_data)
                    }
                    is Resource.Success -> {
                        sharedViewModel.loading_books.observe(viewLifecycleOwner) { loader ->
                            if (loader) {
                                progressBar.visibility = View.VISIBLE
                            } else {
                                progressBar.visibility = View.GONE
                            }
                        }
                        if (args.bookName.isNullOrEmpty()) {
                            bind(response.data?.items?.get(0))
                        } else {
                            val volumeInfoList = response.data?.items?.find {
                                it.volumeInfo?.title == args.bookName
                            }
                            bind(volumeInfoList)
                        }
                    }

                    else -> {
                        requireContext().showToast(R.string.default_error)
                    }
                }
            }
        }
    }

    private fun bind(bookItem: Items?) {
        binding.apply {
            bookNameDetails.text = bookItem?.volumeInfo?.title
            bookAuthorDetails.text =
                bookItem?.volumeInfo?.authors?.get(0) ?: "Authors not found"
            releaseDateNumber.text = bookItem?.volumeInfo?.publishedDate
            numberOfPages.text = bookItem?.volumeInfo?.pageCount.toString()
            paragraphText.text = bookItem?.volumeInfo?.description
            bookImageDetails.loadImageFromUrl(bookItem?.volumeInfo?.imageLinks?.thumbnail)
            buyBook.setOnClickListener {
                openLinkBrowser(
                    bookItem?.volumeInfo?.title.toString(),
                    requireContext()
                )
            }
            shareBook.setOnClickListener {
                val bookDetailsIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT, getString(
                            R.string.share_book,
                            bookItem?.volumeInfo?.title,
                            bookItem?.volumeInfo?.authors?.get(0)
                                ?: "Authors not found",
                            bookItem?.volumeInfo?.pageCount.toString(),
                            bookItem?.volumeInfo?.publishedDate
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
    }
}