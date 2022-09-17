package com.example.ibrasaloonapp.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


val TOKEN = stringPreferencesKey("token")
val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
val USER_ID = stringPreferencesKey("user_id")
val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
val USER_LAST_NAME = stringPreferencesKey("user_last_name")
val PHONE = stringPreferencesKey("phone")
val EXPIRES_IN = intPreferencesKey("expiresIn")


fun MutablePreferences.insertAuthData(authData: AuthData) {
    this[TOKEN] = authData.token
    this[REFRESH_TOKEN] = authData.refreshToken
    this[USER_ID] = authData.user.id
    this[USER_FIRST_NAME] = authData.user.firstName
    this[USER_LAST_NAME] = authData.user.lastName
    this[PHONE] = authData.user.phone
    this[EXPIRES_IN] = authData.expiresIn
}


fun MutablePreferences.clearAuthData() {
    this[TOKEN] = ""
    this[REFRESH_TOKEN] = ""
    this[USER_ID] = ""
    this[USER_FIRST_NAME] = ""
    this[USER_LAST_NAME] = ""
    this[PHONE] = ""
    this[EXPIRES_IN] = 0
}


fun Preferences.getAuthData(): AuthData? {
    if (this[USER_ID] == null || this[TOKEN] == null)
        return null

    return AuthData(
        User(
            id = this[USER_ID] ?: "",
            firstName = this[USER_FIRST_NAME] ?: "",
            lastName = this[USER_LAST_NAME] ?: "",
            phone = this[PHONE] ?: "",
            role = ""
        ),
        token = this[TOKEN] ?: "",
        refreshToken = this[REFRESH_TOKEN] ?: "",
        expiresIn = this[EXPIRES_IN] ?: 0
    )
}