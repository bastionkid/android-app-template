package com.azuredragon.core.ui.compose.components

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.azuredragon.core.ui.compose.theme.AppTheme

@Composable
fun SurfaceWithAppTheme(
    backgroundColor: Color = Color.Unspecified,
    statusBarColor: Color = Color.Unspecified,
    navigationBarColor: Color = Color.Unspecified,
    content: @Composable () -> Unit,
) {
    AppTheme(
        statusBarColor = statusBarColor,
        navigationBarColor = navigationBarColor,
    ) {
        val color = if (backgroundColor == Color.Unspecified) {
            AppTheme.colors.uiBackground
        } else {
            backgroundColor
        }

        Surface(color = color) {
            content()
        }
    }
}