package org.example.geoblinker.presentation.di

import org.example.geoblinker.presentation.features.auth.AuthViewModel
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.example.geoblinker.presentation.viewmodels.MapViewModel
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    // Auth
    viewModel { AuthViewModel(get(), get()) }
    
    // Devices - STAGE 2
    viewModel { DeviceViewModel(get(), get()) }
    
    // Map - STAGE 3 NEW
    viewModel { MapViewModel(get(), get()) }
    
    // Profile - будет обновлен в STAGE 4
    viewModel { ProfileViewModel() }
}
