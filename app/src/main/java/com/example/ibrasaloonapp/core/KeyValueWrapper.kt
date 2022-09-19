package com.example.ibrasaloonapp.core



data class KeyValueWrapper<T, K>(
    val key: T,
    var value: K
)