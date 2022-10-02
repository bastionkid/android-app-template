package com.azuredragon.core.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions

fun Fragment.navigateSafely(
    @IdRes currentDestinationId: Int,
    directions: NavDirections,
    navOptions: NavOptions? = null,
): Boolean {
    return if (isAdded) {
        val navController = findNavController()
        if (navController.currentDestination?.id == currentDestinationId) {
            lifecycleScope.navigateSafely(currentDestinationId, navController, directions.actionId, directions.arguments, navOptions)

            true
        } else {
            false
        }
    } else {
        false
    }
}

fun Fragment.navigateToDeepLinkSafely(
    @IdRes currentDestinationId: Int,
    deepLink: String,
    navOptions: NavOptions? = null,
): Boolean {
    return if (isAdded) {
        val navController = findNavController()
        if (navController.currentDestination?.id == currentDestinationId) {
            lifecycleScope.navigateToDeepLinkSafely(currentDestinationId, navController, deepLink, navOptions)

            true
        } else {
            false
        }
    } else {
        false
    }
}

fun Fragment.navigateUpSafely(@IdRes currentDestinationId: Int): Boolean {
    return if (isAdded) {
        val navController = findNavController()

        if (navController.currentDestination?.id == currentDestinationId) {
            lifecycleScope.launchWhenStarted {
                navController.navigateUp()
            }
            true
        } else {
            false
        }
    } else {
        false
    }
}

private fun LifecycleCoroutineScope.navigateSafely(
    @IdRes currentDestinationId: Int,
    navController: NavController,
    @IdRes targetDestinationId: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions? = null,
) {
    launchWhenStarted {
        if (navController.currentDestination?.id == currentDestinationId) {
            navController.navigate(
                resId = targetDestinationId,
                args = bundle,
                navOptions = navOptions,
                navigatorExtras = FragmentNavigatorExtras()
            )
        } else {
            // TODO(akashkhunt): 24/09/22 Log an event here
        }
    }
}

private fun LifecycleCoroutineScope.navigateToDeepLinkSafely(
    @IdRes currentDestinationId: Int,
    navController: NavController,
    deepLink: String,
    navOptions: NavOptions? = null,
) {
    launchWhenStarted {
        if (navController.currentDestination?.id == currentDestinationId) {
            navController.navigate(
                deepLink = Uri.parse(deepLink),
                navOptions = navOptions,
                navigatorExtras = FragmentNavigatorExtras()
            )
        } else {
            // TODO(akashkhunt): 24/09/22 Log an event here
        }
    }
}

fun Fragment.getNavOptionsForPopToSelf(): NavOptions? {
    return if (isAdded) {
        val navController = findNavController()

        navController.currentDestination?.id?.let { currentDestinationId ->
            navOptions {
                popUpTo(currentDestinationId) {
                    inclusive = true
                }
            }
        }
    } else {
        null
    }
}

fun Fragment.getNavOptionsForPopUpTo(
    @IdRes currentDestinationId: Int,
    isInclusive: Boolean,
): NavOptions? {
    return if (isAdded) {
        val navController = findNavController()

        if (navController.currentDestination?.id == currentDestinationId) {
            navOptions {
                popUpTo(currentDestinationId) {
                    inclusive = isInclusive
                }
            }
        } else {
            null
        }
    } else {
        null
    }
}

fun Fragment.getNavOptionsForPopUpToInclusive(@IdRes currentDestinationId: Int): NavOptions? {
    return getNavOptionsForPopUpTo(currentDestinationId, true)
}

fun Fragment.getNavOptionsForClearBackstack(@IdRes currentDestinationId: Int): NavOptions? {
    return if (isAdded) {
        val navController = findNavController()

        if (navController.currentDestination?.id == currentDestinationId) {
            navOptions {
                popUpTo(navController.backQueue.first().destination.id) {
                    inclusive = true
                }
            }
        } else {
            null
        }
    } else {
        null
    }
}