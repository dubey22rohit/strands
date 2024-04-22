package com.example.strands.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.strands.components.StrandItem
import com.example.strands.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavHostController) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val strandsAndUsers by homeViewModel.strandsAndUsers.observeAsState(null)

    LazyColumn {
        items(strandsAndUsers ?: emptyList()) { pairs ->
            StrandItem(
                strand = pairs.first,
                user = pairs.second,
                navController,
                FirebaseAuth.getInstance().currentUser?.uid,
            )
        }
    }
}
