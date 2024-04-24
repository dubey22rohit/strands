package com.example.strands.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.strands.components.StrandItem
import com.example.strands.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUser(navController: NavHostController, uid: String) {

    val userViewModel: UserViewModel = viewModel()
    val strands by userViewModel.threads.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)

    userViewModel.fetchStrands(uid)
    userViewModel.fetchUser(uid)

    ConstraintLayout(
        modifier = Modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .fillMaxHeight()
            .padding(16.dp)
    ) {

        val (image, username, bio, logoutButton, posts) = createRefs()

        users?.let {
            Text(text = it.username, style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            ),
                modifier = Modifier.constrainAs(username) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )
        }

        Image(
            painter = rememberAsyncImagePainter(model = users?.imageUrl),
            contentDescription = "user image",
            modifier = Modifier
                .constrainAs(image) {
                    end.linkTo(parent.end)
                }
                .clip(CircleShape)
                .size(72.dp),
            contentScale = ContentScale.Crop
        )

        users?.bio?.let {
            Text(
                text = it,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .constrainAs(bio) {
                        top.linkTo(username.bottom, margin = 12.dp)
                        start.linkTo(username.start)
                    }
                    .width((LocalConfiguration.current.screenWidthDp * 0.7).dp)
            )
        }

        ElevatedButton(
            onClick = { },
            modifier = Modifier
                .constrainAs(logoutButton) {
                    top.linkTo(bio.bottom, margin = 24.dp)
                }
                .width(128.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            ),
        ) {
            Text(text = "Follow")
        }

        LazyColumn(modifier = Modifier.constrainAs(posts) {
            top.linkTo(logoutButton.bottom, margin = 24.dp)
        }) {
            if (strands != null && users != null) {
                items(strands ?: emptyList()) { pair ->
                    StrandItem(
                        strand = pair,
                        user = users!!,
                        navController,
                        userId = FirebaseAuth.getInstance().currentUser?.uid
                    )
                }
            }
        }
    }
}
