package com.example.strands.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.strands.navigation.Routes
import com.example.strands.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun Login(navController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val error by authViewModel.error.observeAsState()

    val context = LocalContext.current

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    //TODO: Fix the error
    LaunchedEffect(error) {
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Login", style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        )

        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(text = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(text = "Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please enter the required credentials.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    authViewModel.login(email, password, context)
                }
            }
        ) {
            Text(
                text = "Login",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            )
        }

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(Routes.Register.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        ) {
            Text(
                text = "Don't have an account? Register->",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            )
        }
    }
}