package com.example.strands.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.strands.R
import com.example.strands.navigation.Routes
import com.example.strands.utils.SharedPref
import com.example.strands.viewmodel.AddStrandViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddStrands(navController: NavHostController) {
    val context = LocalContext.current

    val threadViewModel: AddStrandViewModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState(false)

    var strand by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
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

    LaunchedEffect(isPosted) {
        if (isPosted) {
            strand = ""
            imageUri = null
            Toast.makeText(context, "New Post Added!", Toast.LENGTH_SHORT).show()

            navController.navigate(Routes.Home.routes) {
                popUpTo(Routes.AddStrands.routes) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val (crossPic, text, logo, username, editText, attachMedia, button, imageBox) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "close",
            modifier = Modifier
                .constrainAs(crossPic) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {
                    navController.navigate(Routes.Home.routes) {
                        popUpTo(Routes.AddStrands.routes) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
        )

        Text(text = "Add Strand", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ),
            modifier = Modifier.constrainAs(text) {
                top.linkTo(crossPic.top)
                start.linkTo(crossPic.end, margin = 12.dp)
            }
        )

        Image(
            painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)),
            contentDescription = "user image",
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(text.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                }
                .clip(CircleShape)
                .size(36.dp),
            contentScale = ContentScale.FillBounds
        )

        Text(text = SharedPref.getUsername(context), style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
            modifier = Modifier.constrainAs(username) {
                top.linkTo(logo.top)
                start.linkTo(logo.end, margin = 12.dp)
                bottom.linkTo(logo.bottom)
            }
        )

        BasicTextFieldWithHint(
            hint = "Start typing...",
            value = strand,
            onValueChange = { strand = it },
            modifier = Modifier.constrainAs(editText) {
                top.linkTo(username.bottom, margin = 12.dp)
                start.linkTo(username.start)
            }
        )

        if (imageUri == null) {
            Image(
                painter = painterResource(id = R.drawable.baseline_attachment_24),
                contentDescription = "attach image",
                modifier = Modifier
                    .constrainAs(attachMedia) {
                        top.linkTo(editText.bottom)
                        start.linkTo(editText.start)
                    }
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED

                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    }
            )
        } else {
            Box(modifier = Modifier
                .background(Color.Gray)
                .padding(12.dp)
                .constrainAs(imageBox) {
                    top.linkTo(editText.bottom)
                    start.linkTo(editText.start)
                    end.linkTo(parent.end)
                }
                .height(250.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "close",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "remove image",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                            imageUri = null
                        }
                )
            }
        }

        ElevatedButton(
            onClick = {
                if (imageUri == null) {
                    FirebaseAuth.getInstance().currentUser?.let {
                        threadViewModel.saveData(
                            strand = strand,
                            userId = it.uid,
                            imageUri = ""
                        )
                    }
                } else {
                    FirebaseAuth.getInstance().currentUser?.let {
                        threadViewModel.saveImage(
                            strand = strand,
                            userId = it.uid,
                            imageUri = imageUri!!
                        )
                    }
                }
            },
            modifier = Modifier.constrainAs(button) {
                end.linkTo(parent.end, margin = 12.dp)
                bottom.linkTo(parent.bottom, margin = 12.dp)
            }
        ) {
            Text(
                text = "Post",
                style = TextStyle(fontSize = 20.sp)
            )
        }

    }
}

@Composable
fun BasicTextFieldWithHint(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(text = hint, color = Color.Gray)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = Color.Black),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}