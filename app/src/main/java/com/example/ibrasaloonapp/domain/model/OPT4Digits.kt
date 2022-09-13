package com.example.ibrasaloonapp.domain.model

data class OPT4Digits(
    val one: String,
    val two: String,
    val three: String,
    val four: String
) {

    constructor(value: String) : this(value,value,value,value)



    fun getCode(): String {
        return "${one}${two}${three}${four}"
    }

    fun isEmpty(): Boolean {
        return one.isEmpty() && two.isEmpty() && three.isEmpty() && four.isEmpty()
    }
}