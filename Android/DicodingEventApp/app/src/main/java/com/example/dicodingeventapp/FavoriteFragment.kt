package com.example.dicodingeventapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.databinding.FragmentFavoriteBinding
import com.example.dicodingeventapp.ui.adapter.EventAdapter
import com.example.dicodingeventapp.viewmodel.FavoriteViewModel
import com.example.dicodingeventapp.viewmodel.ViewModelFactory
import com.example.dicodingeventapp.data.model.Event

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFavoriteBinding.bind(view)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_ID, event.id)
                putExtra(DetailActivity.EXTRA_NAME, event.name)
                putExtra(DetailActivity.EXTRA_IMAGE, event.imageLogo)
                putExtra(DetailActivity.EXTRA_OWNER, event.ownerName)
                putExtra(DetailActivity.EXTRA_TIME, event.beginTime)
                val quotaLeft = event.quota - event.registrants
                putExtra(DetailActivity.EXTRA_QUOTA, quotaLeft)
                putExtra(DetailActivity.EXTRA_DESC, event.description)
                putExtra(DetailActivity.EXTRA_LINK, event.link)
            }
            startActivity(intent)
        }

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@FavoriteFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.getFavorites().observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNullOrEmpty()) {
                binding.rvFavorite.visibility = View.GONE
                binding.txtEmpty.visibility = View.VISIBLE
            } else {
                binding.txtEmpty.visibility = View.GONE
                binding.rvFavorite.visibility = View.VISIBLE

                val eventList = favorites.map {
                    Event(
                        id = it.id,
                        name = it.name,
                        ownerName = it.ownerName ?: "",
                        imageLogo = it.mediaCover ?: "",
                        beginTime = it.beginTime ?: "",
                        quota = it.quota,
                        registrants = it.registrants,
                        description = it.description ?: "",
                        link = it.link ?: ""
                    )
                }
                adapter.submitList(eventList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}