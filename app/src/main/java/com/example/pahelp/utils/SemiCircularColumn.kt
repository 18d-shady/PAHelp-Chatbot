package com.example.pahelp.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pahelp.R


@Composable
fun SemiCircularColumn(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawSemiCircle(color = color)
        }

        Image(
            painter = painterResource(id = R.drawable.main_icon),
            contentDescription = "Icon",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .align(Alignment.TopCenter) // Positioning the image above the semi-circle
                .padding(top = 50.dp) // You can adjust this value to change the position
        )

    }
}

fun DrawScope.drawSemiCircle(color: Color) {
    val width = size.width
    val height = size.height

    // Draw a semi-circle
    drawArc(
        color = color,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = androidx.compose.ui.geometry.Offset(0f, height/2),
        size = androidx.compose.ui.geometry.Size(width, height)
    )
}