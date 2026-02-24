package com.example.dicodingeventapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dicodingeventapp.databinding.FragmentSettingBinding
import com.example.dicodingeventapp.viewmodel.ThemeViewModel
import com.example.dicodingeventapp.viewmodel.ViewModelFactory

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}