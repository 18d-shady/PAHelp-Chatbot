package com.example.pahelp.screens


import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pahelp.model.Chat
import com.example.pahelp.utils.CustomTopBar
import com.example.pahelp.viewModel.ChatViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavController


@Composable
fun DateScreen(navController: NavController, viewModel: ChatViewModel) {
    val chats by viewModel.chats.collectAsState()

    // Load all chats when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.getAllChats()
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Previous Chats",
                showBackButton = true,
                onBackPressed = { navController.popBackStack() },
                options = listOf(
                    if (viewModel.isAdmin.value) {
                        "Disable Admin" to {
                            viewModel.toggleAdmin()
                        }
                    } else {
                        "Enable Admin" to {
                            navController.navigate("login_admin_screen")
                        }
                    }
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (chats.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "No chats available.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                } else {

                    items(chats) { chat ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .border(
                                    BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable {
                                    navController.navigate("past_chats_screen/${Uri.encode(chat.date)}")
                                }
                        ) {
                            Text(
                                text = "Chats on ${chat.date}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}