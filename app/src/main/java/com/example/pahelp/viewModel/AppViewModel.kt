package com.example.pahelp.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    var isAuthenticated = mutableStateOf(false)
        private set

    var isPasswordCorrect = mutableStateOf(false)
        private set

    private val correctPassword = "winston"

    // Function to validate the password
    fun validatePassword(input: String) {
        isPasswordCorrect.value = input == correctPassword
    }

    fun sendMessage(input: String){

    }

    // Function to perform login action
    fun login() {
        isAuthenticated.value = true
    }
}