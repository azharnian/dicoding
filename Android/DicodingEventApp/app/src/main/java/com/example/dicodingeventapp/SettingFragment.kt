package com.example.dicodingeventapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.*
import java.util.concurrent.TimeUnit
import com.example.dicodingeventapp.databinding.FragmentSettingBinding
import com.example.dicodingeventapp.viewmodel.ThemeViewModel
import com.example.dicodingeventapp.viewmodel.ViewModelFactory
import com.example.dicodingeventapp.worker.ReminderWorker

class SettingFragment : Fragment(R.layout.fragment_setting) {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val themeViewModel: ThemeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSettingBinding.bind(view)

        themeViewModel.isDarkTheme.observe(viewLifecycleOwner) { isDark ->
            binding.switchTheme.isChecked = isDark
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (binding.switchTheme.isPressed) {
                themeViewModel.saveTheme(isChecked)
            }
        }

        themeViewModel.isReminderActive.observe(viewLifecycleOwner) { isActive ->
            if (binding.switchReminder.isChecked != isActive) {
                binding.switchReminder.isChecked = isActive
            }
        }

        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (binding.switchReminder.isPressed) {
                themeViewModel.saveReminder(isChecked)
                handleReminderWorker(isChecked)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleReminderWorker(isActive: Boolean) {

        val workManager = WorkManager.getInstance(requireContext())

        if (isActive) {

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<ReminderWorker>(
                1, TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "daily_reminder",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )

        } else {

            workManager.cancelUniqueWork("daily_reminder")
        }
    }
}