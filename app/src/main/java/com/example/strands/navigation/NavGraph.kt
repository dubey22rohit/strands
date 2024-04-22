package com.example.strands.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.strands.screens.AddStrands
import com.example.strands.screens.BottomNav
import com.example.strands.screens.Home
import com.example.strands.screens.Login
import com.example.strands.screens.Notification
import com.example.strands.screens.Profile
import com.example.strands.screens.Register
import com.example.strands.screens.Search
import com.example.strands.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.routes) {
        composable(Routes.Splash.routes) {
            Splash(navController)
        }
        composable(Routes.Home.routes) {
            Home(navController)
        }
        composable(Routes.Notification.routes) {
            Notification()
        }
        composable(Routes.Search.routes) {
            Search()
        }
        composable(Routes.AddStrands.routes) {
            AddStrands(navController)
        }
        composable(Routes.Profile.routes) {
            Profile(navController)
        }
        composable(Routes.BottomNav.routes) {
            BottomNav(navController)
        }
        composable(Routes.Login.routes) {
            Login(navController)
        }
        composable(Routes.Register.routes) {
            Register(navController)
        }
    }
}
