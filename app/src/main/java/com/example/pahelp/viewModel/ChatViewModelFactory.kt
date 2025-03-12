package com.example.pahelp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pahelp.db.dao.ChatDao
import com.example.pahelp.db.dao.UserSettingsDao


class ChatViewModelFactory(
    private val chatDao: ChatDao,
    private val userSettingsDao: UserSettingsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(chatDao, userSettingsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}