package com.example.dicodingeventapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.data.model.Event
import com.example.dicodingeventapp.databinding.FragmentHomeBinding
import com.example.dicodingeventapp.ui.adapter.EventAdapter
import com.example.dicodingeventapp.ui.adapter.HomeSliderAdapter
import com.example.dicodingeventapp.utils.ResultState
import com.example.dicodingeventapp.viewmodel.EventViewModel
import com.example.dicodingeventapp.viewmodel.ViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var upcomingAdapter: HomeSliderAdapter
    private lateinit var finishedAdapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        observeViewModel()
        setupSearch()
        setupRetry()

        if (viewModel.upcomingState.value == null || viewModel.finishedState.value == null) {
            viewModel.loadHomeEvents()
        }
    }

    private fun setupRecyclerView() {
        upcomingAdapter = HomeSliderAdapter { event -> openDetail(event) }
        finishedAdapter = EventAdapter { event -> openDetail(event) }

        binding.rvUpcomingHome.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = upcomingAdapter
        }

        binding.rvFinishedHome.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = finishedAdapter
        }
    }

    private fun observeViewModel() {

        viewModel.upcomingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    hideLoading()
                    val upcoming = state.data.take(5)
                    upcomingAdapter.submitList(upcoming)
                    showContent()
                }
                is ResultState.Error -> showError(state.message)
            }
        }

        viewModel.finishedState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    hideLoading()
                    val finished = state.data.take(5)
                    finishedAdapter.submitList(finished)
                    showContent()
                }
                is ResultState.Error -> showError(state.message)
            }
        }

        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> {
                    hideLoading()
                    upcomingAdapter.submitList(state.data)
                    showContent()
                }
                is ResultState.Error -> showError(state.message)
            }
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.search(query)
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            }
        )
    }

    private fun setupRetry() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadHomeEvents()
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
        // Note: keeping layoutContent visible while loading if there's already data might be better,
        // but following existing pattern of hiding it during global loading.
        if (upcomingAdapter.itemCount == 0 && finishedAdapter.itemCount == 0) {
            binding.layoutContent.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showContent() {
        binding.layoutContent.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.txtError.text = message
        binding.layoutError.visibility = View.VISIBLE
        binding.layoutContent.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun openDetail(event: Event) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID, event.id)
            putExtra(DetailActivity.EXTRA_NAME, event.name)
            putExtra(DetailActivity.EXTRA_IMAGE, event.imageLogo)
            putExtra(DetailActivity.EXTRA_OWNER, event.ownerName)
            putExtra(DetailActivity.EXTRA_TIME, event.beginTime)
            putExtra(DetailActivity.EXTRA_QUOTA, event.quota - event.registrants)
            putExtra(DetailActivity.EXTRA_DESC, event.description)
            putExtra(DetailActivity.EXTRA_LINK, event.link)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}