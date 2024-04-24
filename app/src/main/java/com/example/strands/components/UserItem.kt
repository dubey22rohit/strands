package com.example.strands.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.strands.R
import com.example.strands.model.StrandModel
import com.example.strands.model.UserModel
import com.example.strands.navigation.Routes
import com.example.strands.utils.SharedPref

@Composable
fun UserItem(
    user: UserModel,
    navController: NavHostController,
) {
    Column {
        ConstraintLayout(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .fillMaxWidth()
                .clickable {
                    val route = Routes.OtherUser.routes.replace("{uid}", user.uid)
                    navController.navigate(route)
                }
        ) {

            val (userImage, username, bio) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(model = user.imageUrl),
                contentDescription = "user image",
                modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .clip(CircleShape)
                    .size(36.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = user.username,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .constrainAs(username) {
                        top.linkTo(userImage.top)
                        start.linkTo(userImage.end)
                    }
            )

            user.bio?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp)
                        .constrainAs(bio) {
                            top.linkTo(username.bottom)
                            start.linkTo(username.start)

                        }
                )
            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}