package com.example.erabook.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.erabook.activities.AuthenticationViewModel
import com.example.erabook.adapters.FavoriteAdapter
import com.example.erabook.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var layoutManager: GridLayoutManager
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteAdapter = FavoriteAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(
            layoutInflater,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authenticationViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            favoriteViewModel.fetchFavoriteBooks(user?.email.toString())
        }

        layoutManager = GridLayoutManager(requireContext(), 2)

        binding.apply {
            favoriteList.apply {
                adapter = favoriteAdapter
                layoutManager = this@FavoriteFragment.layoutManager
            }
        }
        favoriteViewModel.listOfBooks.observe(viewLifecycleOwner) { listOfBooks ->
            favoriteAdapter.submitList(listOfBooks)
            if (listOfBooks?.isEmpty() == false) {
                binding.progressBar.visibility = View.GONE
            }
        }
        favoriteViewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.apply {
                    favoriteInfo.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    favoriteInfo.visibility = View.VISIBLE
                }
            }
        }

    }
}