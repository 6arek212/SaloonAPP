package com.example.ibrasaloonapp.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.Customer


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


val TOKEN = stringPreferencesKey("token")
val USER_ID = stringPreferencesKey("user_id")
val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
val USER_LAST_NAME = stringPreferencesKey("user_last_name")
val PHONE = stringPreferencesKey("phone")
val EXPIRES_IN = intPreferencesKey("expiresIn")


fun MutablePreferences.insertAuthData(authData: AuthData) {
    this[TOKEN] = authData.token
    this[USER_ID] = authData.customer.id
    this[USER_FIRST_NAME] = authData.customer.firstName
    this[USER_LAST_NAME] = authData.customer.lastName
    this[PHONE] = authData.customer.phone
    this[EXPIRES_IN] = authData.expiresIn
}

fun Preferences.getAuthData(): AuthData {
    return AuthData(
        Customer(
            id = this[USER_ID] ?: "",
            firstName = this[USER_FIRST_NAME] ?: "",
            lastName = this[USER_LAST_NAME] ?: "",
            phone = this[PHONE] ?: ""
        ),
        token = this[TOKEN] ?: "",
        expiresIn = this[EXPIRES_IN] ?: 0
    )
}