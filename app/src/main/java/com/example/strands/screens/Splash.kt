package com.example.strands.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.strands.R
import com.example.strands.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (image) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.threads_logo),
            contentDescription = "logo",
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
        )
    }
    LaunchedEffect(key1 = true) {
        delay(500)
        if (FirebaseAuth.getInstance().currentUser != null)
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        else
            navController.navigate(Routes.Login.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
    }

}
