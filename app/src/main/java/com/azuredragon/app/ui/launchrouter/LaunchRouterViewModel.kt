package com.azuredragon.app.ui.launchrouter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LaunchRouterViewModel : ViewModel() {

	private val viewModelState = MutableStateFlow<LaunchRoute?>(null)

	val launchRoute = viewModelState.stateIn(
		viewModelScope,
		SharingStarted.WhileSubscribed(),
		viewModelState.value,
	)

	init {
		viewModelScope.launch {
			viewModelState.value = when (true) {
				true -> LaunchRoute.Login
				else -> LaunchRoute.Home
			}
		}
	}
}
