package com.example.ibrasaloonapp.presentation.ui.users_list

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import com.example.ibrasaloonapp.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.user_details.UserDetailsEvent
import com.example.ibrasaloonapp.presentation.ui.user_details.UserDetailsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun UserDetails(viewModel: UserDetailsViewModel, popBackStack: () -> Unit) {
    val user = viewModel.user.collectAsState(initial = null).value
    val showBlockDialog = viewModel.state.value.showBlockDialog
    val appointmentCount = viewModel.state.value.appointmentCount
    val paid = viewModel.state.value.paid
    val refresh = viewModel.state.value.refresh

    if (user != null)
        UserDetailsView(
            refresh = refresh,
            user = user,
            paid = paid,
            appointmentCount = appointmentCount,
            showBlockDialog = showBlockDialog,
            popBackStack = popBackStack,
            onRefresh = {
                viewModel.onTriggerEvent(UserDetailsEvent.Refresh)
            },
            onBlockDialog = { visible ->
                viewModel.onTriggerEvent(
                    UserDetailsEvent.BlockDialogVisibility(
                        visible
                    )
                )
            }
        )
}


@Composable
fun UserDetailsView(
    refresh: Boolean,
    user: User,
    paid: Double?,
    appointmentCount: Int?,
    showBlockDialog: Boolean,
    onBlockDialog: (Boolean) -> Unit,
    popBackStack: () -> Unit,
    onRefresh: () -> Unit
) {
    val swipeState = rememberSwipeRefreshState(isRefreshing = refresh)
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

    SwipeRefresh(state = swipeState, onRefresh = onRefresh) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Brush.verticalGradient(listOf(Black2, Black1)))
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BackButton(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Start), onClick = popBackStack, tint = White
                )

                if (user.superUser != null && user.superUser) {
                    val painter = rememberAsyncImagePainter(R.drawable.crown)
                    Image(
                        modifier = Modifier
                            .size(60.dp),
                        painter = painter,
                        contentDescription = "crown"
                    )
                }

                VerticalImageChip(
                    imageSize = 160.dp,
                    url = user.image,
                    text = user.role,
                    onClick = {},
                    isSelected = false
                )

                Spacer(modifier = Modifier.padding(8.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (user.role == "customer") {
                        Icon(
                            imageVector = Icons.Outlined.SupervisedUserCircle,
                            contentDescription = "customer",
                            tint = White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Work,
                            contentDescription = "worker",
                            tint = White
                        )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onPrimary
                    )
                }


                Spacer(modifier = Modifier.padding(16.dp))


                Row {

                    StatsCard(
                        title = stringResource(id = R.string.appointments),
                        value = appointmentCount?.let { "$it" } ?: "",
                        visible = true,
                        leadingIcon = Icons.Outlined.AutoGraph
                    )


                    Spacer(modifier = Modifier.padding(16.dp))
                    StatsCard(
                        title = stringResource(R.string.paid),
                        value = paid?.let { "$it₪" } ?: "",
                        visible = true,
                        leadingIcon = Icons.Outlined.Money
                    )

                }

                Spacer(modifier = Modifier.padding(16.dp))


                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Joined at 12/08/2020",
                    color = Black1,
                    style = MaterialTheme.typography.caption
                )
            }



            Spacer(modifier = Modifier.padding(16.dp))

            Column(modifier = Modifier.padding(8.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
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
                        }
                        .border(BorderStroke(1.dp, Gray1), shape = MaterialTheme.shapes.medium)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconText(
                        text = "${stringResource(id = R.string.call)} - ${user.phone}",
                        icon = Icons.Filled.Phone,
                        contentDescription = "",
                        textStyle = MaterialTheme.typography.body2
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))


                    Icon(
                        imageVector = Icons.Outlined.ArrowForwardIos,
                        contentDescription = "right arrow",
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onBlockDialog(true) }
                        .border(BorderStroke(1.dp, Gray1), shape = MaterialTheme.shapes.medium)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconText(
                        text = "Mark As Barber",
                        icon = Icons.Filled.WorkHistory,
                        contentDescription = "",
                        textStyle = MaterialTheme.typography.body2
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))


                    Icon(
                        imageVector = Icons.Outlined.ArrowForwardIos,
                        contentDescription = "right arrow",
                        modifier = Modifier.size(12.dp)
                    )
                }


                Spacer(modifier = Modifier.padding(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onBlockDialog(true) }
                        .border(BorderStroke(1.dp, Gray1), shape = MaterialTheme.shapes.medium)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconText(
                        text = stringResource(R.string.block),
                        icon = Icons.Filled.Block,
                        contentDescription = "",
                        textStyle = MaterialTheme.typography.body2
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))


                    Icon(
                        imageVector = Icons.Outlined.ArrowForwardIos,
                        contentDescription = "right arrow",
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }


    if (showBlockDialog)
        QuestionDialog(
            title = "Are you sure?",
            description = "You want to block Tarik Husin",
            actionButtons = true,
            onConfirm = { },
            onDismiss = { onBlockDialog(false) })
}


@Composable
@Preview
fun CustomerDetailsPreview() {
    AppTheme {

        UserDetailsView(
            refresh = false,
            user = User("", "Tarik", "Husin", "0525145565", "barber", superUser = true),
            appointmentCount = 54,
            paid = 40.0,
            onBlockDialog = { s -> },
            showBlockDialog = false,
            popBackStack = {},
            onRefresh = {}
        )
    }
}
