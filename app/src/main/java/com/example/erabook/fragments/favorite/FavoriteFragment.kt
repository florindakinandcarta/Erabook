package com.example.erabook.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.erabook.R
import com.example.erabook.adapters.FavoriteAdapter
import com.example.erabook.databinding.FragmentFavoriteBinding
import com.example.erabook.util.showToast
import com.google.firebase.auth.FirebaseAuth


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var layoutManager: GridLayoutManager
    private val firebaseAuth = FirebaseAuth.getInstance()


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
        favoriteAdapter = FavoriteAdapter { position ->
            favoriteViewModel.removeFavoriteBook(
                firebaseAuth.currentUser?.email.toString(),
                position
            )
        }
        favoriteViewModel.fetchFavoriteBooks(firebaseAuth.currentUser?.email.toString())
        favoriteViewModel.isRemoved.observe(viewLifecycleOwner) { isRemoved ->
            if (isRemoved) {
                requireActivity().showToast(R.string.favorite_removed)
            } else {
                requireActivity().showToast(R.string.default_error)
            }
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