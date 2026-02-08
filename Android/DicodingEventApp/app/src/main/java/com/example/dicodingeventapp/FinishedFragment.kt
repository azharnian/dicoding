package com.example.dicodingeventapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.databinding.FragmentFinishedBinding
import com.example.dicodingeventapp.ui.adapter.EventAdapter
import com.example.dicodingeventapp.viewmodel.EventViewModel

class FinishedFragment : Fragment(R.layout.fragment_finished) {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFinishedBinding.bind(view)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadFinished()

        binding.btnRetry.setOnClickListener {

            binding.layoutError.visibility = View.GONE
            viewModel.loadFinished()
        }

    }

    private fun setupRecyclerView() {

        binding.rvFinished.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {

        viewModel.events.observe(viewLifecycleOwner) { events ->

            binding.rvFinished.adapter = EventAdapter(events) { event ->

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
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {

                binding.progressBar.visibility = View.VISIBLE
                binding.layoutError.visibility = View.GONE

            } else {

                binding.progressBar.visibility = View.GONE
            }
        }


        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->

            if (!errorMessage.isNullOrEmpty()) {

                binding.layoutError.visibility = View.VISIBLE
                binding.rvFinished.visibility = View.GONE

                binding.txtError.text = errorMessage
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}