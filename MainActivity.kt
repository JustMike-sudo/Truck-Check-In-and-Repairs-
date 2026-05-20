package com.valentinesgarage.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.valentinesgarage.data.local.AppDatabase
import com.valentinesgarage.data.repository.GarageRepository
import com.valentinesgarage.ui.screens.GarageNavHost
import com.valentinesgarage.ui.theme.ValentineGarageTheme
import com.valentinesgarage.ui.viewmodel.*

/**
 * Shell activity that hosts Jetpack Compose Navigation for Valentine's Garage.
 * Completely transitions the app to an offline-first Room Database architecture.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room database & repository with lifecycleScope for seeding
        val database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = GarageRepository(database.garageDao())

        // Setup ViewModels
        val authViewModel: AuthViewModel by viewModels()

        val factory = GarageViewModelFactory(repository)
        val checkInViewModel: CheckInViewModel by viewModels { factory }
        val activeJobsViewModel: ActiveJobsViewModel by viewModels { factory }
        val checklistViewModel: ChecklistViewModel by viewModels { factory }
        val reportsViewModel: ReportsViewModel by viewModels { factory }
        val adminViewModel: AdminViewModel by viewModels { factory }

        setContent {
            ValentineGarageTheme {
                GarageNavHost(
                    authViewModel = authViewModel,
                    checkInViewModel = checkInViewModel,
                    activeJobsViewModel = activeJobsViewModel,
                    checklistViewModel = checklistViewModel,
                    reportsViewModel = reportsViewModel,
                    adminViewModel = adminViewModel
                )
            }
        }
    }
}
