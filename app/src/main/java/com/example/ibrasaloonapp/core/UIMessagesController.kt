package com.example.ibrasaloonapp.core

import com.example.ibrasaloonapp.core.domain.UIComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class UIMessagesController private constructor() {

    companion object {
        private var instance: UIMessagesController? = null

        fun getInstance(): UIMessagesController {
            synchronized(this) {
                if (instance == null) {
                    instance = UIMessagesController()
                }
                return instance as UIMessagesController
            }
        }
    }


    private val _message: MutableSharedFlow<UIComponent?> = MutableSharedFlow()
    val message: SharedFlow<UIComponent?> = _message


    suspend fun sendMessage(uiComponent: UIComponent) {
        _message.emit(uiComponent)
    }

    suspend fun removeMessage() {
        _message.emit(UIComponent.None)
    }
}