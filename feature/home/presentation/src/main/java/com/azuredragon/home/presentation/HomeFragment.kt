package com.azuredragon.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.azuredragon.core.ui.compose.composeWithSurface

class HomeFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		return composeWithSurface(requireContext()) {
			Box(modifier = Modifier.fillMaxSize()) {
				Text(
					text = stringResource(id = R.string.label_home),
					modifier = Modifier.align(Alignment.Center),
				)
			}
		}
	}
}
