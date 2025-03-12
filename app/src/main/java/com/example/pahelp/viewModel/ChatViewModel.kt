package com.example.pahelp.viewModel

import java.io.File
import java.io.FileOutputStream
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.pahelp.db.dao.ChatDao
import com.example.pahelp.db.dao.UserSettingsDao
import com.example.pahelp.model.AdminUtil
import com.example.pahelp.model.Chat
import com.example.pahelp.model.ChatCompletionRequest
import com.example.pahelp.model.ChatMessage
import com.example.pahelp.model.Message
import com.example.pahelp.model.UserSettings
import com.example.pahelp.network.RetrofitInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class ChatViewModel(
    private val chatDao: ChatDao,
    private val userSettingsDao: UserSettingsDao
) : ViewModel() {

    var isAdmin = mutableStateOf(false) // Admin status
        private set

    fun toggleAdmin() {
        isAdmin.value = !isAdmin.value
    }


    val showLoader = mutableStateOf(false)
    var wallpaperUri = mutableStateOf<Uri?>(null)


    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats = _chats.asStateFlow()

    fun saveWallpaperUri(uri: Uri?, context: Context,) {
        uri?.let {
            val stableUri = copyImageToAppStorage(context, it)
            wallpaperUri.value = stableUri
            viewModelScope.launch {
                val settings = UserSettings(wallpaperUri = stableUri?.toString())
                userSettingsDao.insertUser(settings)
            }
        }
    }


    fun loadWallpaperUri() {
        viewModelScope.launch {
            val settings = userSettingsDao.getUser ()
            wallpaperUri.value = settings?.wallpaperUri?.let { Uri.parse(it) }
            Log.d("ChatViewModel", "Loaded wallpaper URI: ${wallpaperUri.value}") // Debug log
        }
    }

    fun copyImageToAppStorage(context: Context, uri: Uri): Uri? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val outputFile = File(outputDir, "wallpaper_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(outputFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return Uri.fromFile(outputFile) // Save this URI to the database
    }

    fun deleteWallpaperImage(context: Context, wallpaperUri: Uri?) {
        wallpaperUri?.let {
            val file = File(it.path)
            if (file.exists()) {
                file.delete()
            }
        }
    }

    fun removeWallpaper(context: Context,) {
        viewModelScope.launch {

            deleteWallpaperImage(context, wallpaperUri.value)

            wallpaperUri.value = null

            val settings = UserSettings(wallpaperUri = null)
            userSettingsDao.insertUser(settings)
        }
    }


    suspend fun getResponse(message: String): String {

        if (AdminUtil().adminMessages.prompts.any { it.equals(message, ignoreCase = true) }) {
            val adminResponses = AdminUtil().adminMessages.responses
            return adminResponses[Random.nextInt(adminResponses.size)]
        } else {
            // Create the request for the API call
            val request = ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    ChatMessage(role = "user", content = message)
                ),
                temperature = 0.7
            )

            return try {

                val response = RetrofitInstance.service.getChatCompletion(request)

                if (response.choices.isNotEmpty()) {
                    response.choices[0].message.content
                } else {
                    "No response from OpenAI."
                }
            } catch (e: Exception) {
                "Failed to reach OpenAI: ${e.message}"
            }
        }
    }


    fun saveMessage(content: String, sending: Boolean, date: String) {
        val message = Message(
            content = content,
            sending = sending,
            time = System.currentTimeMillis().toString()
        )
        println(message.time)

        viewModelScope.launch {
            val chat = chatDao.getChatByDate(date)
            if (chat != null) {
                // Update existing chat
                val updatedMessages = chat.messages.toMutableList()
                updatedMessages.add(message)
                chatDao.updateChat(chat.copy(messages = updatedMessages))
            } else {
                // Insert new chat
                chatDao.insertChat(Chat(date = date, messages = mutableListOf(message)))
            }

            // Update the messages state
            _messages.value = chatDao.getChatByDate(date)?.messages ?: emptyList()
        }
    }

    fun deleteMessage(message: Message, date: String) {
        viewModelScope.launch {
            val chat = chatDao.getChatByDate(date)
            chat?.let {
                val updatedMessages = it.messages.toMutableList()
                updatedMessages.remove(message)
                chatDao.updateChat(it.copy(messages = updatedMessages))
                // Update the messages state
                _messages.value = chatDao.getChatByDate(date)?.messages ?: emptyList()
            }
        }
    }

    fun getMessagesForDate(date: String) {
        viewModelScope.launch {
            val chat = chatDao.getChatByDate(date)
            _messages.value = chat?.messages ?: emptyList()
        }
    }

    suspend fun getChatByDate(date: String?): Chat? {
        return if (date == null) {
            null
        } else {
            val chat = chatDao.getChatByDate(date)
            chat?.let { filterMessages(it) }
        }
    }


    fun getAllChats() {
        viewModelScope.launch {
            val allChats = chatDao.getAllChats()
            _chats.value = allChats
        }
    }


    private fun filterMessages(chat: Chat): Chat {
        val filteredMessages = chat.messages.filter { message ->
            if (isAdmin.value) {
                true
            } else {
                val isAdminMessage = AdminUtil().adminMessages.prompts.any {
                    it.equals(
                        message.content,
                        ignoreCase = true
                    )
                } ||
                        AdminUtil().adminMessages.responses.any {
                            it.equals(
                                message.content,
                                ignoreCase = true
                            )
                        }

                !isAdminMessage
            }
        }.toMutableList() // Convert to MutableList<Message>

        return chat.copy(messages = filteredMessages)
    }
}

/*
Secure Storage: Use a secure storage solution like Android's EncryptedSharedPreferences or a key store, especially for sensitive data like access tokens.
Environment Config: You can also keep such tokens in environment-specific configurations, making it easier to manage for different environments (e.g., development, staging, production).
 */