package com.example.ibrasaloonapp.presentation.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.SupervisedUserCircle
import androidx.compose.material.icons.outlined.Work
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.navigateToWhatsapp
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.Black1
import com.example.ibrasaloonapp.presentation.theme.Gray1
import com.example.ibrasaloonapp.presentation.theme.Gray2


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserCard(user: User, onClick: () -> Unit) {

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("ExampleScreen", "PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }



    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Gray2,
        onClick = onClick
    ) {

        Row(
            modifier = Modifier

                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (user.superUser != null && user.superUser) {
                    val painter = rememberAsyncImagePainter(R.drawable.crown)
                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .zIndex(1f),
                        painter = painter,
                        contentDescription = "crown"
                    )
                }

                CircularImage(
                    modifier = Modifier.size(46.dp),
                    url = if (user.image != null) "https://saloon-ibra-api.herokuapp.com/imgs/${user.image}" else null,
                    onClick = {})

            }

            Spacer(modifier = Modifier.padding(8.dp))


            Column {

                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground
                )

                Spacer(modifier = Modifier.padding(2.dp))

                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.caption,
                    color = Black1
                )


                Spacer(modifier = Modifier.padding(2.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (user.role == "customer") {
                        Icon(
                            imageVector = Icons.Outlined.SupervisedUserCircle,
                            contentDescription = "customer"
                        )
                    } else {
                        Icon(imageVector = Icons.Outlined.Work, contentDescription = "worker")
                    }

                    if (user.isBlocked) {
                        Icon(imageVector = Icons.Outlined.Block, contentDescription = "blocked")
                    }
                }

            }



            Spacer(modifier = Modifier.weight(1f))


            IconButton(onClick = {
                navigateToWhatsapp(context = context, phone = user.phone)
            }) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.whatsapp),
                    contentDescription = "Whatsapp"
                )
            }

            IconButton(onClick = {

                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CALL_PHONE
                    ) -> {
                        // Some works that require permission
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data =
                            Uri.parse("tel:${user.phone}")
                        ContextCompat.startActivity(context, intent, null)
                    }
                    else -> {
                        // Asking for permission
                        launcher.launch(Manifest.permission.CALL_PHONE)
                    }
                }

            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.call),
                    contentDescription = "Call"
                )
            }


        }


    }
}


@Composable
@Preview
fun UserCardPreview() {
    AppTheme {
        UserCard(
            User(
                id = "",
                firstName = "tarik",
                lastName = "Husin",
                phone = "0525145565",
                role = "barber"
            ),
            onClick = {

            }
        )
    }
}