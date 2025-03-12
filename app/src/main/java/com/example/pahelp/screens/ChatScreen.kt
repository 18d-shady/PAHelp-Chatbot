package com.example.pahelp.screens


import SetWallpaper
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.pahelp.R
import com.example.pahelp.model.Message
import com.example.pahelp.utils.CustomTopBar
import com.example.pahelp.viewModel.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun ChatScreen(navController: NavController, viewModel: ChatViewModel) {
    var message by remember { mutableStateOf("") }
    val messages by viewModel.messages.collectAsState()
    var isFocused by remember { mutableStateOf(false) }
    var selectedMessage: Message? by remember { mutableStateOf(null) }
    val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy"))
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val wallpaperUri by viewModel.wallpaperUri

    // Image picker launcher
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.saveWallpaperUri(uri, context)
            }
        }
    )

    LaunchedEffect(currentDate) {
        viewModel.showLoader.value = true
        viewModel.getMessagesForDate(currentDate)
        viewModel.loadWallpaperUri()
        viewModel.showLoader.value = false
    }

    val options = remember(selectedMessage, wallpaperUri) {
        val baseOptions = mutableListOf(
            "See past messages" to {
                navController.navigate("all_chats_screen")
            },
            "Set Wallpaper" to {
                launcher.launch("image/*")
            }
        )

        if (wallpaperUri != null) {
            baseOptions.add("Remove Wallpaper" to {
                viewModel.removeWallpaper(context)
            })
        }

        selectedMessage?.let {
            baseOptions.addAll(
                listOf(
                    "Copy" to {
                        selectedMessage?.let { messageToCopy ->
                            val clip = ClipData.newPlainText("Copied Message", messageToCopy.content)
                            clipboardManager.setPrimaryClip(clip)
                        }
                    },
                    "Delete" to {
                        selectedMessage?.let { messageToDelete ->
                            viewModel.deleteMessage(messageToDelete, date = currentDate)
                            selectedMessage = null
                        }
                    }
                )
            )
        }
        baseOptions
    }


    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Chat",
                options = options,
                iconPath = painterResource(id = R.drawable.main_icon)
            )
        },

        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .imePadding()
                    .navigationBarsPadding()
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .onFocusChanged { focusState ->
                            // Track if the input field is focused
                            isFocused = focusState.isFocused
                        },
                    shape = RoundedCornerShape(30.dp),
                    maxLines = 5
                )

                IconButton(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(3.dp),
                    onClick = {
                        coroutineScope.launch {
                            if (message.isNotBlank()) {
                                viewModel.saveMessage(
                                    message,
                                    sending = true,
                                    date = currentDate
                                )

                                viewModel.showLoader.value = true

                                val theReply = viewModel.getResponse(message)
                                message = ""

                                delay(400)
                                viewModel.saveMessage(
                                    theReply,
                                    sending = false,
                                    date = currentDate
                                )

                                //viewModel.saveMessage("Hey there", sending = false, date = currentDate)
                                viewModel.showLoader.value = false
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        // Box that contains the content
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
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                selectedMessage = null
                            }
                        )
                    },
            ) {
                items(messages) { msg ->
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
                            )
                        ,
                        horizontalArrangement = if (msg.sending) {
                            Arrangement.End
                        } else {
                            Arrangement.Start
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .background(color= if (msg.sending) {
                                        MaterialTheme.colorScheme.tertiary
                                    } else{
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
            LaunchedEffect(messages) {
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(messages.size - 1)
                }
            }
        }
    }
}

