package com.example.pahelp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pahelp.viewModel.ChatViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(viewModel: ChatViewModel) {
    if (viewModel.showLoader.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingDots()
        }
    }
}


@Composable
fun LoadingDots() {
    val dotSize = 16.dp
    var dotOffset by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            dotOffset = (dotOffset + 1) % 3
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .size(dotSize)
                .offset(y = if (dotOffset == 0) 20.dp else 0.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(dotSize)
                .offset(y = if (dotOffset == 1) 20.dp else 0.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(dotSize)
                .offset(y = if (dotOffset == 2) 20.dp else 0.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
    }
}
