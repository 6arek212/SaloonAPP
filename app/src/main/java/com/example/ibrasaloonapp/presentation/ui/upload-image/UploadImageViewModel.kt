package com.example.ibrasaloonapp.presentation.ui.upload

import android.app.Application
import android.webkit.MimeTypeMap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class UploadImageViewModel
@Inject
constructor(
    private val context: Application,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private var stream: InputStream? = null

    private val _uploadState: MutableState<UploadImageState> = mutableStateOf(UploadImageState())
    val uploadState: State<UploadImageState> = _uploadState

    private val _events = Channel<UploadUIEvent>()
    val events = _events.receiveAsFlow()


    fun onTriggerEvent(event: UploadImageEvent) {
        viewModelScope.launch {
            when (event) {
                is UploadImageEvent.OnRemoveUIComponent -> {
                    removeHeadMessage()
                }

                is UploadImageEvent.OnSelectedImage -> {
                    _uploadState.value =
                        _uploadState.value.copy(imageUri = event.imageUri, buttonVisible = true)
                    stream = event.imageStream
                }

                is UploadImageEvent.Upload -> {
                    uploadImage()
                }
            }
        }
    }


    private suspend fun uploadImage() {
        val st = stream
        val uri = _uploadState.value.imageUri

        if (st == null || uri == null) {
            appendToMessageQueue(
                UIComponent.Dialog(
                    title = "You did not pick an image !",
                    description = "you have to click the image above and select a new image"
                )
            )
            return
        }

        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        val type = mime.getExtensionFromMimeType(cR.getType(uri)) ?: return

        loading(true)
        val result = userRepository.uploadImage(st, type)

        when (result) {
            is ApiResult.Success -> {
//                _uploadState.value = _uploadState.value.copy(imageUri = null)
                _events.send(UploadUIEvent.ImageUploaded(result.value))
            }
            is ApiResult.GenericError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Error",
                        description = result.errorMessage
                    )
                )
                if (result.code == 401) {
                    sendUiEvent(MainUIEvent.Logout)
                }
            }

            is ApiResult.NetworkError -> {
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = context.getString(R.string.error),
                        description = context.getString(R.string.something_went_wrong)
                    )
                )
            }
        }


        loading(false)
    }


}