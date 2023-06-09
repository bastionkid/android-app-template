package com.azuredragon.core.ui.compose

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.azuredragon.core.ui.R

@Composable
fun ShowLoadingAnimation(modifier: Modifier = Modifier) {
	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

	LottieAnimation(
		composition = composition,
		restartOnPlay = true,
		iterations = Int.MAX_VALUE,
		modifier = modifier.fillMaxSize(),
	)
}

@Composable
fun ShowLottieAnimation(
	@RawRes lottieRes: Int,
	modifier: Modifier = Modifier,
) {
	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))

	LottieAnimation(
		composition = composition,
		restartOnPlay = true,
		iterations = Int.MAX_VALUE,
		modifier = modifier,
	)
}
