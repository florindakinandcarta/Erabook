package com.example.erabook.fragments.discoverGoogle

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.erabook.R
import com.example.erabook.adapters.DiscoverAdapter
import com.example.erabook.databinding.FragmentDiscoverBinding
import com.example.erabook.util.showToast
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : Fragment() {
    private lateinit var binding: FragmentDiscoverBinding
    private val googleBooksViewModel: GoogleBooksViewModel by viewModels()
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

            searchBarGoogle.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val searchTerm = query?.replace(" ", "+")
                    googleBooksViewModel.fetchBooks(searchTerm, 0)
                    loadMore.setOnClickListener {
                        googleBooksViewModel.fetchBooks(searchTerm, 20)
                    }
                    searchBarGoogle.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            googleBooksViewModel.response_books.observe(viewLifecycleOwner) { response ->
                sharedViewModel.setResponseBooks(response)
                when (response) {
                    is Resource.Error -> {
                        requireContext().showToast(R.string.error_fetching_data)
                    }

                    is Resource.Success -> {
                        googleBooksViewModel.loading_books.observe(viewLifecycleOwner) { loader ->
                            if (loader) {
                                binding.apply {
                                    loadingBooks.visibility = View.VISIBLE
                                }

                            } else {
                                binding.apply {
                                    loadingBooks.visibility = View.GONE
                                    loadMore.visibility = View.VISIBLE
                                }
                            }
                        }
                        val volumeInfoList = response.data?.items?.map { it.volumeInfo }
                        volumeInfoList?.let { discoverAdapter.submitList(it) }
                    }

                    else -> {
                        requireContext().showToast(R.string.default_error)
                    }
                }

            }
            qrCodeButton.setOnClickListener {
                scanBook()
            }
        }
    }

    private fun scanBook() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_QR_CODE
            )
            .enableAutoZoom()
            .build()
        val scanner = GmsBarcodeScanning.getClient(requireContext())
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val barcodeString = "=isbn:${barcode.rawValue}"
                googleBooksViewModel.fetchBooksWithISBN(barcodeString)
                googleBooksViewModel.response_single_book.observe(viewLifecycleOwner) { singleBook ->
                    when (singleBook) {
                        is Resource.Error -> {
                            requireContext().showToast(R.string.error_fetching_data)
                        }

                        is Resource.Success -> {
                            val bookItem = bundleOf(Pair("bookName", singleBook.data?.items?.map { it.volumeInfo?.title }))
                            findNavController().navigate(R.id.discoverToDetails, bookItem)
                            googleBooksViewModel.loading_books.observe(viewLifecycleOwner) { loader ->
                                if (loader) {
                                    binding.apply {
                                        loadingBooks.visibility = View.VISIBLE
                                    }

                                } else {
                                    binding.apply {
                                        loadingBooks.visibility = View.GONE
                                        loadMore.visibility = View.VISIBLE
                                    }
                                }
                            }

                        }

                        else -> {
                            requireContext().showToast(R.string.default_error)
                        }
                    }
                }
            }
            .addOnCanceledListener {
                requireContext().showToast(R.string.cancelled)
            }
            .addOnFailureListener { message ->
                println("Some error happened: $message")
                requireContext().showToast(R.string.default_error)
            }
    }
}