package com.example.dicodingeventapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.dicodingeventapp.databinding.ActivityMainBinding
import com.example.dicodingeventapp.viewmodel.ThemeViewModel
import com.example.dicodingeventapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val themeViewModel: ThemeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeViewModel.isDarkTheme.observe(this) { isDark ->
            val currentMode = AppCompatDelegate.getDefaultNightMode()

            if (isDark && currentMode != AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            if (!isDark && currentMode != AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked = true
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_upcoming -> {
                    loadFragment(UpcomingFragment())
                    true
                }
                R.id.nav_finished -> {
                    loadFragment(FinishedFragment())
                    true
                }
                R.id.nav_favorite -> {
                    loadFragment(FavoriteFragment())
                    true
                }
                R.id.nav_setting -> {
                    loadFragment(SettingFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
