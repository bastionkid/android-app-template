package com.azuredragon.app.ui.launchrouter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.azuredragon.app.R
import com.azuredragon.core.ui.compose.composeWithSurface
import com.azuredragon.core.ui.navigation.getNavOptionsForPopUpToInclusive
import com.azuredragon.core.ui.navigation.navigateToDeepLinkSafely

class LaunchRouterFragment: Fragment() {

    private val viewModel: LaunchRouterViewModel by viewModels()

    @OptIn(ExperimentalLifecycleComposeApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return composeWithSurface(
            context = requireContext(),
            backgroundColor = Color.Transparent,
            systemBarColor = Color.Transparent,
        ) {
            val uiState by viewModel.launchRoute.collectAsStateWithLifecycle()

            when (uiState) {
                LaunchRoute.Login -> {
                    navigateToDeepLink("app://azuredragon.com/login")
                }
                LaunchRoute.Home -> {
                    navigateToDeepLink("app://azuredragon.com/home")
                }
                else -> {
                    // default is null so no need to do anything here
                }
            }
        }
    }

    private fun navigateToDeepLink(deepLink: String) {
        navigateToDeepLinkSafely(
            currentDestinationId = R.id.launchRouterFragment,
            deepLink = deepLink,
            navOptions = getNavOptionsForPopUpToInclusive(R.id.launchRouterFragment),
        )
    }
}