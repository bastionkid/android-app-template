package com.azuredragon.app.ui.launchrouter

sealed interface LaunchRoute {

    object Login: LaunchRoute

    object Home: LaunchRoute
}