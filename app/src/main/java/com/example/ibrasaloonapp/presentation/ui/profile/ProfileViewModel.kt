package com.example.ibrasaloonapp.presentation.ui.profile


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.network.ApiResult
import com.example.ibrasaloonapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    val userRepository: UserRepository
) : ViewModel() {


    private val _state: MutableState<ProfileState> = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    init {
        onTriggerEvent(ProfileEvent.GetUser)
    }

    fun onTriggerEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                is ProfileEvent.GetUser -> {
                    getUser()
                }
            }
        }
    }


    private suspend fun getUser() {

        val result = userRepository.getUser()

        when (result) {
            is ApiResult.Success -> {
                _state.value = _state.value.copy(user = result.value)
            }
            is ApiResult.GenericError -> {

            }
            is ApiResult.NetworkError -> {
            }
        }
    }

}