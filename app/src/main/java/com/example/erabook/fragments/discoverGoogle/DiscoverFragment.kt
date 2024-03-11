package com.example.erabook.fragments.discoverGoogle

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.erabook.databinding.FragmentDiscoverBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment: Fragment() {
    private lateinit var binding: FragmentDiscoverBinding
    private val googleBooksViewModel: GoogleBooksViewModel by viewModels()

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

        binding.getData.setOnClickListener {
            googleBooksViewModel.fetchBooks()
            googleBooksViewModel.response_books.observe(viewLifecycleOwner){response ->
                when(response){
                    is Resource.Error -> {
                        println("Error fetching data")
                    }
                    is Resource.Success -> {
                        println(response.data?.items)
                    }

                    else -> {}
                }

            }
        }
    }

}