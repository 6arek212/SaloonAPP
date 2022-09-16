package com.example.ibrasaloonapp.presentation.ui.upload

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.ProgressBarState
import com.example.ibrasaloonapp.presentation.components.CircularImage
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.ProgressButton
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.Black4
import com.example.ibrasaloonapp.presentation.ui.profile.ProfileViewModel
import java.io.InputStream

const val IMAGE_KEY = "image_key"

private const val TAG = "UploadImageView"

@Composable
fun UploadImageView(
    navController: NavController,
    viewModel: UploadImageViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel?,
    popBackStack: (String) -> Unit
) {
    val progress = viewModel.uiState.value.progressBarState
    val events = viewModel.events
    val buttonVisible = viewModel.uploadState.value.buttonVisible
    val selectedImageUri = viewModel.uploadState.value.imageUri
    val selectedImageUrl = profileViewModel?.state?.value?.user?.image
    val uiMessage = viewModel.uiState.value.uiMessage

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is UploadUIEvent.ImageUploaded -> {
                    popBackStack(event.imagePath)
                }
            }
        }
    }

    DefaultScreenUI(
        uiComponent = uiMessage,
        onRemoveUIComponent = { viewModel.onTriggerEvent(UploadImageEvent.OnRemoveUIComponent) }) {
        UploadImage(
            buttonVisible = buttonVisible,
            onClose = { navController.popBackStack() },
            loading = progress == ProgressBarState.Loading,
            selectedImageUri = selectedImageUri,
            selectedImageUrl = selectedImageUrl,
            onUpload = { viewModel.onTriggerEvent(UploadImageEvent.Upload) },
            onSelectedImage = { stream, uri ->
                viewModel.onTriggerEvent(
                    UploadImageEvent.OnSelectedImage(
                        stream, uri
                    )
                )
            }
        )
    }

}


@Composable
fun UploadImage(
    loading: Boolean,
    onClose: () -> Unit,
    onSelectedImage: (InputStream?, Uri?) -> Unit,
    selectedImageUrl: String?,
    selectedImageUri: Uri?,
    onUpload: () -> Unit,
    buttonVisible: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        IconButton(modifier = Modifier.align(Alignment.Start), onClick = onClose) {

            Icon(imageVector = Icons.Filled.Close, contentDescription = "exit")
        }

        UploadImageTop(
            selectedImageUrl = selectedImageUrl,
            selectedImageUri = selectedImageUri,
            onSelectedImage = onSelectedImage
        )


        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(visible = buttonVisible) {
            ProgressButton(
                loading = loading,
                onClick = onUpload,
                color = MaterialTheme.colors.primary,
                progressColor = MaterialTheme.colors.background
            ) {
                Text(text = stringResource(R.string.upload), style = MaterialTheme.typography.body1)
            }
        }
    }
}


@Composable
fun UploadImageTop(
    modifier: Modifier = Modifier,
    selectedImageUrl: String?,
    selectedImageUri: Uri?,
    onSelectedImage: (InputStream?, Uri?) -> Unit
) {
    val context = LocalContext.current

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val stream = context.contentResolver.openInputStream(uri)
                onSelectedImage(stream, uri)
            }
        }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d(TAG, "PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d(TAG, "PERMISSION DENIED")
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                Log.d(TAG, "Code requires permission")
            }
            else -> {
                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularImage(
            modifier = Modifier
                .padding(16.dp)
                .size(200.dp),
            onClick = {
                galleryLauncher.launch("image/*")
            },
            uri = selectedImageUri,
            url = if (selectedImageUrl != null) "https://saloon-ibra-api.herokuapp.com/imgs/" + selectedImageUrl
            else null
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.upload_image),
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground,
            fontSize = 40.sp
        )

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.click_image_above_and_pick_an_image),
            style = MaterialTheme.typography.body2,
            color = Black4
        )
    }


}


@Composable
@Preview
fun UploadImagePreview(

) {
    AppTheme {
        UploadImage(
            loading = true,
            onSelectedImage = { s, u -> },
            onClose = {},
            selectedImageUri = null,
            selectedImageUrl = null,
            onUpload = {},
            buttonVisible = true
        )


    }
}



