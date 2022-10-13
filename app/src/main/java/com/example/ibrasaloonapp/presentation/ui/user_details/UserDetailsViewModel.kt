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
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.network.Resource
import com.example.ibrasaloonapp.presentation.BaseViewModel
import com.example.ibrasaloonapp.repository.AuthRepository
import com.example.ibrasaloonapp.use.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel() {

    private val _state: MutableState<UserDetailsState> = mutableStateOf(UserDetailsState())
    val state: State<UserDetailsState> = _state

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: Flow<User?> = _user.map { value ->
        value?.let {
            val role = when (it.role) {
                "barber" -> context.getString(R.string.barber)
                "customer" -> context.getString(R.string.customer)
                else -> it.role
            }

            User(
                id = it.id,
                firstName = it.firstName,
                lastName = it.lastName,
                phone = it.phone,
                role = role,
                image = it.image,
                superUser = it.superUser
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

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

                is UserDetailsEvent.BlockDialogVisibility -> {
                    _state.value = _state.value.copy(showBlockDialog = event.visible)
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
                        _user.emit(user)
                        _state.value =
                            _state.value.copy(appointmentCount = appointmentCount, paid = paid)
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