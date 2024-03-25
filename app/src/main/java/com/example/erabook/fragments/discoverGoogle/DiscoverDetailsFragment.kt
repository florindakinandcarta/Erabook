package com.example.erabook.fragments.discoverGoogle

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.erabook.R
import com.example.erabook.activities.AuthenticationViewModel
import com.example.erabook.data.models.Items
import com.example.erabook.databinding.FragmentBookDetailsBinding
import com.example.erabook.util.loadImageFromUrl
import com.example.erabook.util.openLinkBookDownload
import com.example.erabook.util.openLinkBrowser
import com.example.erabook.util.showToast
import com.example.erabook.util.startBookDetailsIntent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private val args: DiscoverFragmentArgs by navArgs()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
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
            when (bookItem?.accessInfo?.viewability) {
                "ALL_PAGES" -> {
                    bookItem.accessInfo.pdf?.let { pdf ->
                        if (pdf.isAvailable == true) {
                            isDownloadableButton.setOnClickListener {
                                openLinkBookDownload(
                                    pdf.downloadLink,
                                    requireContext()
                                )
                            }
                        } else {
                            isDownloadableButton.setImageResource(R.drawable.no_download)
                        }
                    }

                    authenticationViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
                        favoriteBook.setOnClickListener {
                            sharedViewModel.saveBookToDB(bookItem, user?.email.toString())
                            sharedViewModel.isSaved.observe(viewLifecycleOwner) { value ->
                                if (value) {
                                    favoriteBook.setImageResource(R.drawable.favorite)
                                    requireActivity().showToast(R.string.favorite_added)
                                } else {
                                    requireActivity().showToast(R.string.update_message_error)
                                }
                            }
                        }
                    }
                    shareBook.setOnClickListener {
                        bookItem.volumeInfo?.let {
                            requireActivity().startBookDetailsIntent(
                                it.title,
                                it.authors[0],
                                it.pageCount.toString(),
                                it.publishedDate
                            )
                        }
                    }
                }
            }
        }
    }
}