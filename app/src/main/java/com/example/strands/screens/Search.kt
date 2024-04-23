package com.example.strands.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.strands.components.UserItem
import com.example.strands.viewmodel.SearchViewModel

@Composable
fun Search(navController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel()
    val userList by searchViewModel.userList.observeAsState()

    var search by remember {
        mutableStateOf("")
    }

    Column {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text(text = "Search User") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            if (userList != null && userList!!.isNotEmpty()) {
                val filteredUserList =
                    userList!!.filter { it.username.contains(search, ignoreCase = true) }
                items(filteredUserList) {
                    UserItem(user = it, navController = navController)
                }
            }
        }
    }
}
