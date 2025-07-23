package com.spacex.di

import com.spacex.viewmodel.FalconDetailViewModel
import com.spacex.presentation.favorite.FavoriteViewModel
import com.spacex.presentation.falcons.FalconsViewModel
import com.spacex.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModule = module {
    viewModelOf(::FalconsViewModel)
    viewModelOf(::FalconDetailViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::FavoriteViewModel)
}