package org.example.geoblinker.presentation.features.map_screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun WebViewMap(
    modifier: Modifier = Modifier,
    onMapReady: (MapController) -> Unit,
    onMarkerClick: (String) -> Unit // Callback, когда JS вызывает AndroidInterface
)