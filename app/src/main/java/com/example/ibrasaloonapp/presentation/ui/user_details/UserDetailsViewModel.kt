package com.example.ibrasaloonapp.presentation.ui.user_details

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.domain.UIComponent
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.network.model.UserUpdateDto
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.use.GetUserUseCase
import com.example.ibrasaloonapp.use.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "UserDetailsViewModel"

@HiltViewModel
class UserDetailsViewModel
@Inject
constructor(
    private val context: Application,
    private val savedState: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel() {

    private val _state: MutableState<UserDetailsState> = mutableStateOf(UserDetailsState())
    val state: State<UserDetailsState> = _state


    private var userId: String? = null

    init {
        Log.d(TAG, " ProfileViewModel ")
        val userId = savedState.get<String>("userId")
        this.userId = userId
        userId?.let {
            onTriggerEvent(UserDetailsEvent.GetUser(userId))
        }
    }

    fun onTriggerEvent(event: UserDetailsEvent) {
        viewModelScope.launch {
            when (event) {
                is UserDetailsEvent.GetUser -> {
                    getUser(event.userId)
                }

                is UserDetailsEvent.Refresh -> {
                    userId?.let {
                        _state.value = _state.value.copy(refresh = true)
                        getUser(it)
                        _state.value = _state.value.copy(refresh = false)
                    }
                }


                is UserDetailsEvent.MarkAsBarber -> {
                    updateUser(markAsBarber = event.value)
                }


                is UserDetailsEvent.Block -> {
                    updateUser(blocked = event.value)
                }

            }
        }
    }


    private suspend fun getUser(userId: String) {

        getUserUseCase(userId).collect {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let {
                        val (user, appointmentCount, paid) = it
                        _state.value =
                            _state.value.copy(
                                user = user,
                                appointmentCount = appointmentCount,
                                paid = paid
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
        }
    }


    private suspend fun updateUser(
        blocked: Boolean? = null,
        markAsBarber: Boolean? = null
    ) {
        val id = userId ?: return
        val userUpdate =
            UserUpdateDto(
                isBlocked = blocked,
                role = markAsBarber?.let { if (markAsBarber) "barber" else "customer" })



        updateUserUseCase(userUpdate = userUpdate, userId = id).collect {
            when (it) {
                is Resource.Loading -> {
                    loading(it.value)
                }

                is Resource.Success -> {
                    it.data?.let { user->
                        _state.value =
                            _state.value.copy(user = user)
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
        }
    }

}