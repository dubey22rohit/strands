package com.example.strands.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.strands.model.BottomNavItem
import com.example.strands.navigation.Routes

@Composable
fun BottomNav(navHostController: NavHostController) {
    val navController = rememberNavController()

    Scaffold(bottomBar = { MyBottomBar(navController) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = Routes.Home.routes) {
                Home(navController)
            }
            composable(route = Routes.Search.routes) {
                Search(navController)
            }
            composable(route = Routes.AddStrands.routes) {
                AddStrands(navController)
            }
            composable(route = Routes.Notification.routes) {
                Notification()
            }
            composable(route = Routes.Profile.routes) {
                Profile(navHostController)
            }
        }
    }
}

@Composable
fun MyBottomBar(navController: NavHostController) {

    val backStackEntry = navController.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem(
            title = "Home",
            Routes.Home.routes,
            Icons.Rounded.Home,
        ),
        BottomNavItem(
            title = "Search",
            Routes.Search.routes,
            Icons.Rounded.Search,
        ),
        BottomNavItem(
            title = "Add Strands",
            Routes.AddStrands.routes,
            Icons.Rounded.Add,
        ),
        BottomNavItem(
            title = "Notifications",
            Routes.Notification.routes,
            Icons.Rounded.Notifications,
        ),
        BottomNavItem(
            title = "Profile",
            Routes.Profile.routes,
            Icons.Rounded.Person,
        ),
    )

    BottomAppBar {
        list.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = " ")
                }
            )
        }
    }
}
