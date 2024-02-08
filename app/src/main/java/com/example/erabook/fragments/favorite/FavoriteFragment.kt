package com.example.erabook.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.erabook.adapters.FavoriteAdapter
import com.example.erabook.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoriteViewModel: FavoriteViewModel by viewModels()

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

        binding.apply {
            favoriteList.apply {
                adapter = favoriteAdapter
            }
        }

        favoriteAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                favoriteViewModel.saveFavorites(favoriteAdapter.currentList)
            }
        })

        favoriteAdapter.submitList(favoriteViewModel.loadFavorites())

        if (favoriteAdapter.currentList.isEmpty()){
            binding.favoriteInfo.visibility = View.VISIBLE
        }else{
            binding.favoriteInfo.visibility = View.GONE
        }


    }

    override fun onResume() {
        super.onResume()
        favoriteAdapter.submitList(favoriteViewModel.loadFavorites())

    }




}