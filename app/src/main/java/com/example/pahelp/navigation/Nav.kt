package com.example.pahelp.navigation

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import com.example.pahelp.screens.ArchiveScreen
import com.example.pahelp.screens.ChatScreen
import com.example.pahelp.screens.DateScreen
import com.example.pahelp.viewModel.ChatViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Text
import com.example.pahelp.screens.LoginAdminScreen


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavigation(navController: NavHostController, viewModel: ChatViewModel) {
    NavHost(
        navController = navController,
        startDestination = "chat_screen"
    ) {
        composable(
            route = "chat_screen",
        ){
            ChatScreen(navController=navController, viewModel = viewModel)

        }

        composable(
            route = "all_chats_screen"
        ){
            DateScreen(navController=navController, viewModel = viewModel)

        }

        composable("past_chats_screen/{date}") { backStackEntry ->
            val encodedDate = backStackEntry.arguments?.getString("date")
            val decodedDate = Uri.decode(encodedDate)

            ArchiveScreen(navController = navController, viewModel = viewModel, date = decodedDate)
        }

        composable("login_admin_screen") {
            LoginAdminScreen(viewModel, navController)
        }
    }

}