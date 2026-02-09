package org.example.geoblinker.presentation.features.map_screen

import androidx.compose.runtime.Composable

@Composable
expect fun LocationHandler(
    requestLocation: Boolean,
    onLocationReceived: (Double, Double) -> Unit,
    onPermissionDenied: () -> Unit
)