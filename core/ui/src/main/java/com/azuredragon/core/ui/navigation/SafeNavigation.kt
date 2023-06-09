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
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.azuredragon.core.ui.R

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

fun Fragment.navigateToDeepLinkWithPopUpToSelf(
	deepLink: String,
	isInclusive: Boolean,
) {
	val navController = findNavController()

	val currentDestinationId = navController.currentDestination?.id ?: 0

	navigateToDeepLinkSafely(
		currentDestinationId = currentDestinationId,
		deepLink = deepLink,
		navOptions = getNavOptionsForPopUpToSelf(
			currentDestinationId = currentDestinationId,
			isInclusive = isInclusive,
		),
	)
}

fun Fragment.navigateToDeepLinkWithPopUpToSelfInclusive(deepLink: String) {
	navigateToDeepLinkWithPopUpToSelf(
		deepLink = deepLink,
		isInclusive = true,
	)
}

fun Fragment.navigateUpSafely(@IdRes currentDestinationId: Int): Boolean {
	return if (isAdded) {
		val navController = findNavController()

		if (navController.currentDestination?.id == currentDestinationId) {
			navController.navigateUp()
			true
		} else {
			false
		}
	} else {
		false
	}
}

fun Fragment.popUpToSafely(
	@IdRes currentDestinationId: Int,
	@IdRes popUpToId: Int,
): Boolean {
	return if (isAdded) {
		val navController = findNavController()

		if (navController.currentDestination?.id == currentDestinationId) {
			navController.navigateUp()
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
	if (navController.currentDestination?.id == currentDestinationId) {
		navController.navigate(
			resId = targetDestinationId,
			args = bundle,
			navOptions = navOptions,
			navigatorExtras = FragmentNavigatorExtras(),
		)
	} else {
		// TODO(akashkhunt): 24/09/22 Log an event here
	}
}

private fun LifecycleCoroutineScope.navigateToDeepLinkSafely(
	@IdRes currentDestinationId: Int,
	navController: NavController,
	deepLink: String,
	navOptions: NavOptions? = null,
) {
	if (navController.currentDestination?.id == currentDestinationId) {
		navController.navigate(
			deepLink = Uri.parse(deepLink),
			navOptions = navOptions,
		)
	} else {
		// TODO(akashkhunt): 24/09/22 Log an event here
	}
}

fun Fragment.getNavOptionsForPopUpToSelf(
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

fun Fragment.getNavOptionsForPopUpToSelfInclusive(@IdRes currentDestinationId: Int): NavOptions? {
	return getNavOptionsForPopUpToSelf(currentDestinationId, true)
}

fun Fragment.getNavOptionsForClearWholeBackstack(@IdRes currentDestinationId: Int): NavOptions? {
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

fun Fragment.getNavOptionsForClearTillRoot(@IdRes currentDestinationId: Int): NavOptions? {
	return if (isAdded) {
		val navController = findNavController()

		if (navController.currentDestination?.id == currentDestinationId) {
			navOptions {
				popUpTo(navController.backQueue.first().destination.id) {
					inclusive = false
				}
			}
		} else {
			null
		}
	} else {
		null
	}
}

fun Fragment.getNavOptionsForPopUpTo(
	@IdRes currentDestinationId: Int,
	@IdRes popUpToId: Int,
	isInclusive: Boolean,
): NavOptions? {
	return if (isAdded) {
		val navController = findNavController()

		if (navController.currentDestination?.id == currentDestinationId) {
			navOptions {
				popUpTo(popUpToId) {
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

fun Fragment.getNavOptionsForPopUpToInclusive(
	@IdRes currentDestinationId: Int,
	@IdRes popUpToId: Int,
): NavOptions? {
	return getNavOptionsForPopUpTo(
		currentDestinationId = currentDestinationId,
		popUpToId = popUpToId,
		isInclusive = true,
	)
}

private fun NavOptionsBuilder.addDefaultSlideAnim() {
	anim {
		enter = R.anim.slide_in_right
		exit = R.anim.slide_out_left
		popEnter = R.anim.slide_in_left
		popExit = R.anim.slide_out_right
	}
}

fun getDefaultSlideAnimNavOptions(): NavOptions {
	return navOptions { addDefaultSlideAnim() }
}
