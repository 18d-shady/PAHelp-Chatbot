package com.example.pahelp.utils

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CustomTopBar(
    title: String,
    iconPath: Painter? = null,
    options: List<Pair<String, () -> Unit>> = emptyList(),
    showIcon: Boolean = true,
    showBackButton: Boolean = false,
    onBackPressed: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                iconPath?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, color = MaterialTheme.colorScheme.onBackground)
            }
        },
        navigationIcon = {
            if (showBackButton && onBackPressed != null) {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },

        actions = {
            if (showIcon) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .shadow(2.dp)
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    options.forEach { (option, action) ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                action() // Execute the associated function
                                expanded = false // Close the menu after selection
                            },
                            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        )
                    }
                }
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}