package com.example.ibrasaloonapp.presentation.ui.profile


import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.presentation.ui.upload.UploadUIEvent
import com.example.ibrasaloonapp.repository.UserRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.ibrasaloonapp.use.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val context: Application,
    private val savedState: SavedStateHandle,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel() {


    private val _state: MutableState<ProfileState> = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state


    init {
        Log.d(TAG, " ProfileViewModel ")
//        onTriggerEvent(ProfileEvent.GetUser)
    }

    fun onTriggerEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is ProfileEvent.OnRemoveUIComponent -> {
                    removeMessage()
                }
//                is ProfileEvent.UpdateImage -> {
//                    val user = _state.value.user?.copy(image = event.imagePath)
//                    _state.value = _state.value.copy(user = user)
//                }

//                is ProfileEvent.GetUser -> {
//                    getUser()
//                }
            }
        }
    }


//    private suspend fun getUser() {
//        getUserUseCase().onEach {
//            when (it) {
//                is Resource.Loading -> {
//                    loading(it.value)
//                }
//
//                is Resource.Success -> {
//                    it.data?.let { user ->
//                        _state.value = _state.value.copy(user = user)
//                    }
//                }
//
//                is Resource.Error -> {
//                    sendMessage(
//                        UIComponent.Dialog(
//                            title = context.getString(R.string.error),
//                            description = it.message
//                        )
//                    )
//                }
//            }
//        }.launchIn(viewModelScope)
//    }

}