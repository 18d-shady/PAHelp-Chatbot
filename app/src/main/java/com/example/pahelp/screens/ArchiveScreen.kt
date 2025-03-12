package com.example.pahelp.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pahelp.model.Chat
import com.example.pahelp.utils.CustomTopBar
import com.example.pahelp.viewModel.ChatViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.pahelp.model.Message


@Composable
fun ArchiveScreen(navController: NavController, viewModel: ChatViewModel, date: String) {

    val chatState = remember { mutableStateOf<Chat?>(null) }
    val loadingState = remember { mutableStateOf(true) }
    var selectedMessage: Message? by remember { mutableStateOf(null) }
    val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val wallpaperUri by viewModel.wallpaperUri


    LaunchedEffect(date) {
        loadingState.value = true // Start loading
        val chat = viewModel.getChatByDate(date)
        chatState.value = chat
        viewModel.loadWallpaperUri()
        loadingState.value = false  // End loading
    }

    val options = remember(selectedMessage) {
        selectedMessage?.let {
            listOf(
                "Copy" to {
                    selectedMessage?.let { messageToCopy ->
                        val clip = ClipData.newPlainText("Copied Message", messageToCopy.content)
                        clipboardManager.setPrimaryClip(clip)
                    }
                    Unit
                },
                "Delete" to {
                    selectedMessage?.let { messageToDelete ->
                        viewModel.deleteMessage(messageToDelete, date = date)
                        selectedMessage = null
                    }
                    Unit
                }
            )

        } ?: emptyList()
    }

    if (loadingState.value) {
        Text(text = "Loading chat...", modifier = Modifier.padding(16.dp))
    } else {
        chatState.value?.let { chat ->
            Scaffold(
                topBar = {
                    CustomTopBar(
                        title = "Chat on ${chat.date}",
                        options = options,
                        showBackButton = true,
                        onBackPressed = { navController.popBackStack() },
                        )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (wallpaperUri != null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(wallpaperUri),
                                contentDescription = "Wallpaper",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface))
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(chat.messages) { msg ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(
                                        color = if (selectedMessage == msg) {
                                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                                        } else {
                                            Color.Transparent
                                        }
                                    )
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                selectedMessage = msg

                                            }
                                        )
                                    }
                                    .padding(
                                        start = if (msg.sending) 30.dp else 0.dp,
                                        end = if (msg.sending) 0.dp else 30.dp
                                    ),
                                horizontalArrangement = if (msg.sending) {
                                    Arrangement.End
                                } else {
                                    Arrangement.Start
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if (msg.sending) {
                                                MaterialTheme.colorScheme.tertiary
                                            } else {
                                                MaterialTheme.colorScheme.background
                                            },
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                ) {
                                    Text(
                                        text = msg.content,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } ?: run {
            Text(text = "Chat not found", modifier = Modifier.padding(16.dp))
        }
    }
}
