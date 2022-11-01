package com.azuredragon.core.ui.compose

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.azuredragon.core.ui.compose.components.SurfaceWithAppTheme

fun Fragment.composeWithSurface(
    context: Context,
    backgroundColor: Color = Color.Unspecified,
    statusBarColor: Color = Color.Unspecified,
    navigationBarColor: Color = Color.Unspecified,
    content: @Composable () -> Unit,
): View = ComposeView(context).apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

    setContent {
        SurfaceWithAppTheme(
            backgroundColor = backgroundColor,
            statusBarColor = statusBarColor,
            navigationBarColor = navigationBarColor,
            content = content,
        )
    }
}