package com.example.erabook.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.erabook.R
import com.example.erabook.data.models.VolumeInfo
import com.example.erabook.databinding.FragmentBookDetailsBinding
import com.example.erabook.util.loadImageFromUrl
import com.example.erabook.util.openLinkBrowser
import com.example.erabook.util.startBookDetailsIntent

class FavoriteDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val args: FavoriteFragmentArgs by navArgs()
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
                loader.visibility = View.GONE
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        bind(args.favoriteBook)
    }
    private fun bind(favoriteBookVolume: VolumeInfo?) {
        binding.apply {
            if (favoriteBookVolume != null){
                loader.visibility = View.GONE
            }else{
                loader.visibility = View.VISIBLE
            }
            favoriteBookVolume?.let { book ->
                    bookNameDetails.text = book.title
                    bookAuthorDetails.text = book.authors[0]
                    releaseDateNumber.text = book.publishedDate
                    numberOfPages.text = book.pageCount.toString()
                    paragraphText.text = book.description
                    bookImageDetails.loadImageFromUrl(book.imageLinks?.thumbnail)
                    buyBook.setOnClickListener {
                        openLinkBrowser(
                            book.title.toString(),
                            requireContext()
                        )
                    }
                    shareBook.setOnClickListener {
                        requireActivity().startBookDetailsIntent(
                            book.title,
                            book.authors[0],
                            book.pageCount.toString(),
                            book.publishedDate
                        )
                    }
                }
            favoriteBook.visibility = View.GONE
            downloadableButton.setBackgroundResource(R.drawable.no_download)
        }
    }
}