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
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.repository.UserRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.ibrasaloonapp.use.UploadImageUseCase
import com.example.trainingapp.network.NetworkErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class UploadImageViewModel
@Inject
constructor(
    private val context: Application,
    private val uploadImageUseCase: UploadImageUseCase
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
                    removeMessage()
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
            sendMessage(
                UIComponent.Dialog(
                    title = context.getString(R.string.you_did_not_pick_an_image),
                    description = context.getString(R.string.you_have_to_click_the_image_above_and_select_a_new_image)
                )
            )
            return
        }

        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        val type = mime.getExtensionFromMimeType(cR.getType(uri)) ?: return
        uploadImageUseCase(inputStream = st, fileType = type).onEach {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { imagePath ->
                        this.stream = null
                        _events.send(UploadUIEvent.ImageUploaded)
                    }
                }

                is Resource.Error -> {
                    sendMessage(
                        UIComponent.Dialog(
                            title = context.getString(R.string.error),
                            description = it.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


}