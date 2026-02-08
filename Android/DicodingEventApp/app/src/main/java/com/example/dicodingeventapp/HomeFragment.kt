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
import com.example.dicodingeventapp.viewmodel.EventViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        observeViewModel()
        setupSearch()
        setupRetry()

        viewModel.loadUpcoming()
    }

    private fun setupRecyclerView() {

        binding.rvUpcomingHome.layoutManager =
            LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

        binding.rvFinishedHome.layoutManager =
            LinearLayoutManager(context)
    }

    private fun observeViewModel() {

        viewModel.events.observe(viewLifecycleOwner) { events ->

            showContent()

            val upcoming = events.take(5)
            val finished = events.takeLast(5)

            binding.rvUpcomingHome.adapter =
                HomeSliderAdapter(upcoming) { event ->
                    openDetail(event)
                }

            binding.rvFinishedHome.adapter =
                EventAdapter(finished) { event ->
                    openDetail(event)
                }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->

            if (!message.isNullOrEmpty()) {
                showError(message)
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

            showLoading()
            viewModel.loadUpcoming()
        }
    }

    private fun showLoading() {

        binding.progressBar.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
        binding.layoutContent.visibility = View.GONE
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

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("name", event.name)
        intent.putExtra("image", event.imageLogo)
        intent.putExtra("owner", event.ownerName)
        intent.putExtra("time", event.beginTime)

        val quotaLeft = event.quota - event.registrant
        intent.putExtra("quota", quotaLeft.toString())

        intent.putExtra("desc", event.description)
        intent.putExtra("link", event.link)

        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
