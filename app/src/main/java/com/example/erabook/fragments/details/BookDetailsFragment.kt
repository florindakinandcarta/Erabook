package com.example.erabook.fragments.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.erabook.R
import com.example.erabook.databinding.FragmentBookDetailsBinding
import com.example.erabook.util.loadImageFromAssets
import com.google.android.material.bottomnavigation.BottomNavigationView

class BookDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private val args: BookDetailsFragmentArgs by navArgs()
    private lateinit var booksDetailViewModel: BookDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        booksDetailViewModel = ViewModelProvider(
            this,
            BookDetailsViewModelFactory(args.link.toString())
        )[BookDetailsViewModel::class.java]


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailsBinding.inflate(
            layoutInflater,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            bookNameDetails.text = args.title
            bookAuthorDetails.text = args.author
            numberOfPages.text = args.pages.toString()
            releaseDateNumber.text = args.year.toString()
            bookImageDetails.loadImageFromAssets(args.imageLink)
            backBookDetails.setOnClickListener {
                findNavController().navigate(R.id.detailToHome)
            }

            buyBook.setOnClickListener {
                openLinkBrowser(args.link.toString())
            }

            booksDetailViewModel.firstParagraph.observe(viewLifecycleOwner) { paragraph ->
                binding.apply {
                    if (paragraph.isNotEmpty()) {
                        paragraphText.text = paragraph
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                    }
                    (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavBar).visibility =
                        View.GONE
                }
            }

            shareBook.setOnClickListener {
                val bookDetailsIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT, getString(
                            R.string.share_book,
                            args.title,
                            args.author,
                            args.pages.toString(),
                            args.year.toString()
                        )
                    )
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_book_subject))
                }
                val chooserIntent = Intent.createChooser(
                    bookDetailsIntent,
                    getString(R.string.send_details)
                )
                startActivity(chooserIntent)
            }
        }
    }

    private fun openLinkBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.barnesandnoble.com/s/$url"))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavBar).visibility =
            View.VISIBLE

    }

}

