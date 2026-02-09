package org.example.geoblinker.presentation.di

import org.example.geoblinker.presentation.features.auth.AuthViewModel
import org.example.geoblinker.presentation.viewmodels.DeviceViewModel
import org.example.geoblinker.presentation.viewmodels.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    // Auth
    viewModel { AuthViewModel(get(), get()) }
    
    // Device (REFACTORED - теперь с реальными зависимостями)
    viewModel { DeviceViewModel(get(), get(), get()) }
    
    // Profile (будет обновлен в STAGE 3)
    viewModel { ProfileViewModel() }
}
