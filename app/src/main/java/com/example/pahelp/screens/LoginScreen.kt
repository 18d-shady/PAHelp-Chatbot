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
import com.example.pahelp.viewModel.AppViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.pahelp.ui.theme.*
import com.example.pahelp.utils.SemiCircularColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: AppViewModel){
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
                            viewModel.validatePassword(it)

                            if (viewModel.isPasswordCorrect.value) {
                                viewModel.login()
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
                            text = if (viewModel.isPasswordCorrect.value) "Password is correct!" else "Password is incorrect!",
                            color = if (viewModel.isPasswordCorrect.value) Color.White else MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

    }

}



// Preview function
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    // Create a mock ViewModel for preview purposes
    val mockViewModel = AppViewModel()

    PAHelpTheme {
        LoginScreen(mockViewModel)
    }
}
