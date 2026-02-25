package com.example.dicodingeventapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.databinding.FragmentUpcomingBinding
import com.example.dicodingeventapp.ui.adapter.EventAdapter
import com.example.dicodingeventapp.viewmodel.EventViewModel
import com.example.dicodingeventapp.viewmodel.ViewModelFactory
import com.example.dicodingeventapp.utils.ResultState

class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var eventAdapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUpcomingBinding.bind(view)

        setupRecyclerView()
        observeState()

        if (viewModel.upcomingState.value == null) {
            viewModel.loadUpcoming()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.loadUpcoming()
        }
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event ->
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

        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = eventAdapter
        }
    }

    private fun observeState() {
        viewModel.upcomingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> {
                    showLoading(true)
                    binding.layoutError.visibility = View.GONE
                    binding.rvUpcoming.visibility = View.GONE
                }
                is ResultState.Success -> {
                    showLoading(false)
                    binding.rvUpcoming.visibility = View.VISIBLE
                    eventAdapter.submitList(state.data)
                }
                is ResultState.Error -> {
                    showLoading(false)
                    binding.layoutError.visibility = View.VISIBLE
                    binding.txtError.text = state.message
                    binding.rvUpcoming.visibility = View.GONE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}