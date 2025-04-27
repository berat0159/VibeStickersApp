package com.courage.vibestickers.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courage.vibestickers.repository.usecases.app_entry.AppEntryUseCases
import com.courage.vibestickers.view.bottomnavigator.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel(){

    var splashCondition by mutableStateOf(true)
        private set

    var startDestination = mutableStateOf(Route.AppStartNavigation.route)
        private set


    init {
        appEntryUseCases.readAppEntry().onEach{
                shouldStartFromHomeScreen ->
            if (shouldStartFromHomeScreen){
                startDestination.value = Route.StickerNavigation.route
            }else {
                startDestination.value = Route.AppStartNavigation.route
            }
            splashCondition = false
        }.launchIn(viewModelScope)
    }
}