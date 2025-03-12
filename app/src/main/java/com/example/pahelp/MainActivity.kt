package com.example.pahelp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pahelp.screens.LoginScreen
import com.example.pahelp.ui.theme.PAHelpTheme
import com.example.pahelp.viewModel.AppViewModel
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.pahelp.db.AppDatabase
import com.example.pahelp.screens.ChatScreen
import com.example.pahelp.viewModel.ChatViewModel
import com.example.pahelp.navigation.MainNavigation
import com.example.pahelp.screens.LoadingScreen
import com.example.pahelp.viewModel.ChatViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()
    private lateinit var chatViewModel: ChatViewModel

    @OptIn(
        ExperimentalMaterial3Api::class,
        ExperimentalAnimationApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PAHelpTheme {
                Surface{
                    val navController = rememberNavController()

                    // Get the database instance and ChatDao
                    val database = AppDatabase.getDatabase(applicationContext)
                    val chatDao = database.chatDao()
                    val userSettingsDao = database.userSettingsDao()

                    // Create ViewModelFactory and initialize ChatViewModel
                    val factory = ChatViewModelFactory(chatDao, userSettingsDao)
                    chatViewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)


                    if (viewModel.isAuthenticated.value) {
                        MainNavigation(navController = navController, viewModel = chatViewModel)
                    } else {
                        LoginScreen(viewModel)
                    }
                    LoadingScreen(chatViewModel)
                }
            }
        }
    }
}

