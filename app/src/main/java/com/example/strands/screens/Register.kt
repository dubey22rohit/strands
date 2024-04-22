package com.example.strands.screens

import android.Manifest.permission
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.strands.R
import com.example.strands.navigation.Routes
import com.example.strands.viewmodel.AuthViewModel

@Composable
fun Register(navController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var bio by remember {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permission.READ_MEDIA_IMAGES
    } else {
        permission.READ_EXTERNAL_STORAGE
    }
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Register", style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = if (imageUri == null) painterResource(id = R.drawable.person_icon)
            else rememberAsyncImagePainter(model = imageUri),
            contentDescription = "person icon",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {
                    val isGranted = ContextCompat.checkSelfPermission(
                        context, permissionToRequest
                    ) == PackageManager.PERMISSION_GRANTED

                    if (isGranted) {
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionToRequest)
                    }
                },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = {
                Text(text = "Username")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = {
                Text(text = "Bio")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill the required fields", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    authViewModel.register(username, email, password, bio, imageUri!!, context)
                }
            }
        ) {
            Text(
                text = "Register",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            )
        }

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(Routes.Login.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        ) {
            Text(
                text = "Already have an account? Login->",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            )
        }
    }
}