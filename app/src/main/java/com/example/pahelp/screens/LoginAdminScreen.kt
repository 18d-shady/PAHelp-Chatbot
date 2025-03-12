package com.example.pahelp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pahelp.ui.theme.*
import com.example.pahelp.utils.SemiCircularColumn
import com.example.pahelp.viewModel.AppViewModel
import com.example.pahelp.viewModel.ChatViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAdminScreen(chatViewModel: ChatViewModel, navController: NavController){
    val appViewModel: AppViewModel = viewModel()
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
        ,
        color = Color.Black
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical=24.dp)
            ,
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                ,
            ) {
                SemiCircularColumn(color=Blue60)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Blue60)
                        .padding(vertical = 24.dp, horizontal = 10.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("Input Password", color = Color.White)

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = password,
                        onValueChange = {
                            password = it
                            appViewModel.validatePassword(it)

                            if (appViewModel.isPasswordCorrect.value) {
                                chatViewModel.toggleAdmin()
                                navController.popBackStack()
                            }
                        },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        colors =  TextFieldDefaults.outlinedTextFieldColors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Blue80,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Blue80,
                            focusedBorderColor = Blue80,
                            unfocusedBorderColor = Color.LightGray,
                        )
                    )

                    if (password.isNotEmpty()) {
                        Text(
                            text = if (appViewModel.isPasswordCorrect.value) "Password is correct!" else "Password is incorrect!",
                            color = if (appViewModel.isPasswordCorrect.value) Color.White else MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

    }

}


