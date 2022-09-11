package com.example.ibrasaloonapp.presentation.ui.edit_profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "EditProfileViewModel"

@HiltViewModel
class EditProfileViewModel
@Inject
constructor(
    private val savedState: SavedStateHandle,
    private val userRepository: UserRepository
) : BaseViewModel() {

    sealed class UIEvent {
        class UpdateUser(val user: User) : UIEvent()
    }

    private val _state: MutableState<EditProfileState> = mutableStateOf(EditProfileState())
    val state: State<EditProfileState> = _state


    private val _events = Channel<UIEvent>()
    val events = _events.receiveAsFlow()


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
                    removeHeadMessage()
                }
            }
        }
    }


    private suspend fun updateProfile() {
        val firstName = state.value.firstName
        val lastName = state.value.lastName
        val phone = state.value.phone

        loading(true)

        //need validation !

        val userUpdate =
            UserUpdateDto(firstName = firstName, lastName = lastName, phone = phone)


        val result = userRepository.updateUser(userUpdate)
        when (result) {
            is ApiResult.Success -> {
                Log.d(TAG, "updateProfile: updated")
                _events.send(UIEvent.UpdateUser(result.value))
                appendToMessageQueue(
                    UIComponent.Dialog(
                        title = "Updated",
                        "You'r profile has been updated !"
                    )
                )
            }
            is ApiResult.GenericError -> {

            }
            is ApiResult.NetworkError -> {
            }
        }

        loading(false)
    }


}