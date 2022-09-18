package com.example.ibrasaloonapp.presentation.ui.edit_profile

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.presentation.MainUIEvent
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.repository.UserRepository
import com.example.ibrasaloonapp.ui.defaultErrorMessage
import com.example.ibrasaloonapp.use.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "EditProfileViewModel"

@HiltViewModel
class EditProfileViewModel
@Inject
constructor(
    private val context: Application,
    private val savedState: SavedStateHandle,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val authRepository: AuthRepository
) : BaseViewModel() {


    private val _state: MutableState<EditProfileState> = mutableStateOf(EditProfileState())
    val state: State<EditProfileState> = _state

    init {
        val userId = savedState.get<String>("userId")
        val firstName = savedState.get<String>("firstName")
        val lastName = savedState.get<String>("lastName")
        val phone = savedState.get<String>("phone")

        if (userId != null && firstName != null && lastName != null && phone != null) {
            _state.value = _state.value.copy(
                userId = userId,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
            )
        }
    }

    fun onTriggerEvent(event: EditProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is EditProfileEvent.OnFirstNameChanged -> {
                    _state.value = _state.value.copy(firstName = event.name)
                }

                is EditProfileEvent.OnLastNameChanged -> {
                    _state.value = _state.value.copy(lastName = event.last)
                }

                is EditProfileEvent.OnPhoneChanged -> {
                    _state.value = _state.value.copy(phone = event.phone)
                }

                is EditProfileEvent.UpdateProfile -> {
                    updateProfile()
                }

                is EditProfileEvent.OnRemoveHeadFromQueue -> {
                    removeMessage()
                }
            }
        }
    }


    private suspend fun updateProfile() {
        val firstName = state.value.firstName
        val lastName = state.value.lastName
        val phone = state.value.phone
        val userId = authRepository.getUserId() ?: return
        //need validation !

        updateProfileUseCase(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            userId = userId
        ).onEach {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { user ->
                        sendMessage(
                            UIComponent.Dialog(
                                title = context.getString(R.string.updated),
                                context.getString(R.string.your_profile_has_been_updated)
                            )
                        )
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