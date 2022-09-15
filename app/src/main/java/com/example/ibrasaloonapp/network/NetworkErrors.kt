package com.example.trainingapp.network

object NetworkErrors {

    const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
    const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"
    const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
    const val NETWORK_ERROR_UNKNOWN = "Unknown network error"
    const val NETWORK_ERROR = "Network error"
    const val NETWORK_ERROR_TIMEOUT = "Network timeout"
    const val NETWORK_DATA_NULL = "Network data is null"
    const val ERROR_UNKNOWN ="Unknown error"


    const val ERROR_404 = 404
    const val ERROR_400 = 400
    const val ERROR_401 = 401
    const val ERROR_403 = 403


    fun isNetworkError(msg: String): Boolean{
        when{
            msg.contains(UNABLE_TO_RESOLVE_HOST) -> return true
            else-> return false
        }
    }
}