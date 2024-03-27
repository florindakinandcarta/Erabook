package com.example.erabook.fragments.home.tabFragments

import Resource
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var paperBackNonFictionAdapter: NYTAdapter
    private lateinit var seriesBookAdapter: NYTAdapter
    private lateinit var tradeFictionAdapter: NYTAdapter
    private lateinit var adviceHowToMiscellaneousAdapter: NYTAdapter
    private lateinit var paperBackGraphicAdapter: NYTAdapter
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
        binding.paperBackNonfictionList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.seriesBooksList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.tradeFictionPaperbackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.adviceHowToAndMiscellaneousList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.paperbackGraphicBooksList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
                    response.data?.results?.lists?.forEach { listName ->
                        when (listName.listName) {
                            "Paperback Nonfiction" -> {
                                paperBackNonFictionAdapter = NYTAdapter()
                                binding.apply {
                                    paperBackNonfiction.text = listName.listName
                                    paperBackNonfictionList.apply {
                                        adapter = paperBackNonFictionAdapter
                                        paperBackNonFictionAdapter.submitList(listName.books)
                                    }
                                }
                            }

                            "Series Books" -> {
                                seriesBookAdapter = NYTAdapter()
                                binding.apply {
                                    seriesBooks.text = listName.listName
                                    seriesBooksList.apply {
                                        adapter = seriesBookAdapter
                                        seriesBookAdapter.submitList(listName.books)
                                    }
                                }
                            }

                            "Trade Fiction Paperback" -> {
                                tradeFictionAdapter = NYTAdapter()
                                binding.apply {
                                    tradeFictionPaperback.text = listName.listName
                                    tradeFictionPaperbackList.apply {
                                        adapter = tradeFictionAdapter
                                        tradeFictionAdapter.submitList(listName.books)
                                    }
                                }
                            }

                            "Advice How-To and Miscellaneous" -> {
                                adviceHowToMiscellaneousAdapter = NYTAdapter()
                                binding.apply {
                                    adviceHowToAndMiscellaneous.text = listName.listName
                                    adviceHowToAndMiscellaneousList.apply {
                                        adapter = adviceHowToMiscellaneousAdapter
                                        adviceHowToMiscellaneousAdapter.submitList(listName.books)
                                    }
                                }
                            }

                            "Graphic Books and Manga" -> {
                                paperBackGraphicAdapter = NYTAdapter()
                                binding.apply {
                                    paperbackGraphicBooks.text = listName.listName
                                    paperbackGraphicBooksList.apply {
                                        adapter = paperBackGraphicAdapter
                                        paperBackGraphicAdapter.submitList(listName.books)
                                    }
                                }
                            }
                        }

                    }
                }

                else -> {
                    binding.bookAnimation.visibility = View.VISIBLE
                    requireContext().showToast(R.string.default_error)
                }
            }
        }
    }
}