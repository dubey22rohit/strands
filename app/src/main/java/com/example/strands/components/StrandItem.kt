package com.example.strands.components

import androidx.compose.foundation.Image
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.strands.R
import com.example.strands.model.StrandModel
import com.example.strands.model.UserModel
import com.example.strands.utils.SharedPref

@Composable
fun StrandItem(
    strand: StrandModel,
    user: UserModel,
    navController: NavHostController,
    userId: String?,
) {
    Column {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            val (userImage, username, date, time, title, image) = createRefs()

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
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = user.username,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                modifier = Modifier.constrainAs(username) {
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = 12.dp)
                }
            )

            Text(
                text = strand.stringText,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(username.bottom)
                    start.linkTo(username.start)
                }
            )

            if (strand.imageUrl != "") {
                Image(
                    painter = rememberAsyncImagePainter(model = strand.imageUrl),
                    contentDescription = "close",
                    modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}