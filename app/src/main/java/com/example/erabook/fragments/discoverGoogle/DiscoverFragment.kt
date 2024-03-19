package com.example.erabook.fragments.discoverGoogle

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
            loadingBooks.visibility = View.GONE

            searchBarGoogle.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val searchTerm = query?.replace(" ", "+")
                    sharedViewModel.fetchBooks(searchTerm, 0)
                    loadMore.setOnClickListener {
                        sharedViewModel.fetchBooks(searchTerm, 20)
                    }
                    loadingBooks.visibility = View.VISIBLE
                    searchSomething.visibility = View.GONE
                    searchBarGoogle.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            sharedViewModel.response_books.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Error -> {
                        requireContext().showToast(R.string.error_fetching_data)
                    }

                    is Resource.Success -> {
                        val volumeInfoList = response.data?.items?.map { it.volumeInfo }
                        volumeInfoList?.let { discoverAdapter.submitList(it) }
                        loadingBooks.visibility = View.GONE
                        loadMore.visibility = View.VISIBLE
                        searchSomething.visibility = View.GONE
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
                sharedViewModel.fetchBooksWithISBN(barcodeString)
            }
            .addOnCanceledListener {
                requireContext().showToast(R.string.cancelled)
            }
            .addOnFailureListener { message ->
                requireContext().showToast(R.string.default_error)
            }
    }
}